// 随机化算法 用随机投点法计算π值
#include "stdafx.h"
#include "RandomNumber.h"
#include <iostream>
using namespace std;

double Darts(int n);

int main()
{
    int n1 = 100, n2 = 1000, n3 = 1000, n4 = 10000, n5 = 10000000;
    cout << "n1=" << n1 << ",π1=" << Darts(n1) << endl;
    cout << "n2=" << n2 << ",π2=" << Darts(n2) << endl;
    cout << "n3=" << n3 << ",π3=" << Darts(n3) << endl;
    cout << "n4=" << n4 << ",π4=" << Darts(n4) << endl;
    cout << "n5=" << n5 << ",π5=" << Darts(n5) << endl;
    return 0;
}

// 用随机投点法计算π值
double Darts(int n)
{
    static RandomNumber dart;
    int k = 0;

    for (int i = 1; i <= n; i++)
    {
        double x = dart.fRandom();
        double y = dart.fRandom();
        if ((x * x + y * y) <= 1)
        {
            k++;
        }
    }

    return 4 * k / double(n);
}
// 随机化算法 用随机投点法计算定积分
#include "stdafx.h"
#include "RandomNumber.h"
#include <iostream>
using namespace std;

double Darts(int n, double a, double b);
double f(double x);

int main()
{
    int n1 = 100, n2 = 1000, n3 = 1000, n4 = 10000, n5 = 10000000;
    double a = 2.0, b = 3.0;
    cout << "n1=" << n1 << ",r1=" << Darts(n1, a, b) << endl;
    cout << "n2=" << n2 << ",r2=" << Darts(n2, a, b) << endl;
    cout << "n3=" << n3 << ",r3=" << Darts(n3, a, b) << endl;
    cout << "n4=" << n4 << ",r4=" << Darts(n4, a, b) << endl;
    cout << "n5=" << n5 << ",r5=" << Darts(n5, a, b) << endl;
    return 0;
}

/*
 * 基本思想是在矩形区域内随机均匀投点，求出由这些点
 * 产生的函数值的算术平均值，再乘以区间宽度，即可得
 * 出定积分的近似解
 */
double Darts(int n, double a, double b)
{
    static RandomNumber dart;
    double sum = 0.0;
    for (int i = 0; i < n; i++)
    {
        double x = (b - a) * dart.fRandom() + a; // 产生[a,b)之间的随机数
        sum = sum + f(x);
    }
    return (b - a) * sum / n;
}

double f(double x)
{
    return x * x;
}