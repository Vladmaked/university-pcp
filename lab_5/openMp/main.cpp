#include <iostream>
#include "omp.h"

using namespace std;

const int rows = 100;
const int cols = 100;

int arr[rows][cols];

void init_arr();
pair<long long, double> sum_elements(int);
tuple<int, long long, double> min_row_sum(int);

int main()
{

	init_arr();

	omp_set_nested(1);
	double t1 = omp_get_wtime();
	pair<long long, double> total_sum;
	tuple<int, long long, double> min_sum_row;

#pragma omp parallel sections
	{
#pragma omp section
		{
			for (int num_threads : {1, 2, 4, 8, 16, 32}) {
				total_sum = sum_elements(num_threads);
				cout << "Total sum with " << num_threads << " threads: " << total_sum.first << "; time: " << total_sum.second << " seconds" << endl;
			}
		}

#pragma omp section
		{
			for (int num_threads : {1, 2, 4, 8, 16, 32}) {
				min_sum_row = min_row_sum(num_threads);
				cout << "Row with minimal sum with " << num_threads << " threads: " << get<0>(min_sum_row) << "; sum: " << get<1>(min_sum_row) << "; time: " << get<2>(min_sum_row) << " seconds" << endl;
			}
		}
	}
	double t2 = omp_get_wtime();

	cout << "Total time - " << t2 - t1 << " seconds" << endl;
	return 0;
}

void init_arr()
{
	for (int i = 0; i < rows; i++)
	{
		for (int j = 0; j < cols; j++)
		{
			arr[i][j] = rand() % 100;
		}
	}
}

pair<long long, double> sum_elements(int num_threads)
{
	long long sum = 0;
	double t1 = omp_get_wtime();
#pragma omp parallel for reduction(+ : sum) num_threads(num_threads)
	for (int i = 0; i < rows; i++)
	{
		for (int j = 0; j < cols; j++)
		{
			sum += arr[i][j];
		}
	}
	double t2 = omp_get_wtime();

	return make_pair(sum, t2 - t1);
}

tuple<int, long long, double> min_row_sum(int num_threads)
{
	int min_row = 0;
	long long min_sum = 0;
	double t1 = omp_get_wtime();

	for (int j = 0; j < cols; j++)
	{
		min_sum += arr[0][j];
	}

#pragma omp parallel for num_threads(num_threads)
	for (int i = 1; i < rows; i++)
	{
		long long current_sum = 0;
		for (int j = 0; j < cols; j++)
		{
			current_sum += arr[i][j];
		}

		if (current_sum < min_sum)
		{
#pragma omp critical
			if (current_sum < min_sum)
			{
				min_sum = current_sum;
				min_row = i;
			}
		}
	}

	double t2 = omp_get_wtime();

	return make_tuple(min_row, min_sum, t2 - t1);
}