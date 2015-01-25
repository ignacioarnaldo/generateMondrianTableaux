%% LOAD DATA
data = csvread('good_paintings.csv');
datat = data';
cases = num2cell(datat);

%% DEFINE VARIABLES
N = 9;
X1 = 1;
X2 = 2;
X3 = 3;
X4 = 4;
X5 = 5;
X6 = 6;
X7 = 7;
X8 = 8;
X9 = 9;

%% DEFINE NUMBER OF DISCRETE EMISSIONS FOR EACH NODE/VARIABLE
node_sizes = [2 3 3 5 5 5 5 5 5];

%% DEFINE ORDER OF VARIABLES
order = [X1 X2 X3 X4 X5 X6 X7 X8 X9];

%% LEARN DAG WITH K2 ALGORITHM (who depends on who??)
dag_k2 = learn_struct_K2(cases, node_sizes, order, 'max_fan_in', 8);

%% CREATE A BAYESIAN NETWORK WITH THE GENERATED DAG
bnet_k2 = mk_bnet(dag_k2, node_sizes, 'names', {'X1','X2','X3','X4','X5','X6','X7','X8','X9'}, 'discrete', 1:9);


%% INITIALIZE THE CONDITIONAL PROBABILITY DISTRIBUTION FOR EACH VARIABLE
bnet_k2.CPD{X1} = tabular_CPD(bnet_k2, X1);
bnet_k2.CPD{X2} = tabular_CPD(bnet_k2, X2);
bnet_k2.CPD{X3} = tabular_CPD(bnet_k2, X3);
bnet_k2.CPD{X4} = tabular_CPD(bnet_k2, X4);
bnet_k2.CPD{X5} = tabular_CPD(bnet_k2, X5);
bnet_k2.CPD{X6} = tabular_CPD(bnet_k2, X6);
bnet_k2.CPD{X7} = tabular_CPD(bnet_k2, X7);
bnet_k2.CPD{X8} = tabular_CPD(bnet_k2, X8);
bnet_k2.CPD{X9} = tabular_CPD(bnet_k2, X9);

%% MAXIMUM LIKELIHOOD PARAMETER ESTIMATION FROM COMPLETE DATA
bnet_k2 = learn_params(bnet_k2, cases);

%% GENERATE A SAMPLE FROM THE TRAINED BNET
sample = sample_bnet(bnet_k2);
sample = sample';

%% GENERATE 10000 SAMPLES AND STORE IN CSV
NUM_SAMPLES = 10000;
for i=1:NUM_SAMPLES
    sample = sample_bnet(bnet_k2);
    samples(i,:) = cell2mat(sample');
end

%% SELECT THE MOST FREQUENT SAMPLES (TOP 50)
NUM_FINAL_SAMPLES = 50;
B = sortrows(samples);
S = [1;any(diff(B),2)];
[L,S] = regexp(sprintf('%i',S'),'1(0)+','start','end'); 
repeated_rows = B(S,:); % Repeated Rows.
repeat_count = (S-L+1)'; % How often each repeated row appears.
rowsCounts = [repeat_count repeated_rows];
sorted_rowCounts = sortrows(rowsCounts,1);
NUM_FINAL_SAMPLES = min(NUM_FINAL_SAMPLES,length(sorted_rowCounts));
final_samples = sorted_rowCounts(end-NUM_FINAL_SAMPLES+1:end,2:end);


%% WRITE SAMPLES TO FILE
csvwrite('samples_k2.csv',final_samples);
