#include <iostream>
#include <algorithm>
#include <cstring>
using namespace std;
typedef pair<int, int> PII;
typedef long long LL;
const int N = 1e5 + 10;

int fz(int a, int n)
{
    if (n == 1)
        return a;
    if (n % 2)
        return a * fz(a, n / 2) * fz(a, n / 2);
    else
        return fz(a, n / 2) * fz(a, n / 2);
}
int main()
{
    int a, n;
    cin >> a >> n;
    int sum = 0;
    if (n % 2 && n != 1)
        sum = a * fz(a, n - 1);
    else if (!(n % 2) && n != 1)
        sum = fz(a, n);
    if (n == 1)
        sum = a;
    cout << sum << endl;
}