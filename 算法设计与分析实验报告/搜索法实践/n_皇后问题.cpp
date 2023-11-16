#include <iostream>
#include <algorithm>
#include <cstring>
using namespace std;
typedef pair<int, int> PII;
typedef long long LL;
const int N = 20;
char g[N][N];
bool col[N], row[N], dg[N], udg[N];
int n;
void dfs(int u)
{
    if (u == n) // 第 n 行
    {
        for (int i = 0; i < n; i++)
            puts(g[i]);
        cout << endl;
        return;
    }
    for (int i = 0; i < n; i++)
    {
        if (!col[i] && !dg[u + i] && !udg[n - u + i])
        {
            g[u][i] = 'Q';
            col[i] = dg[u + i] = udg[n - u + i] = true;
            dfs(u + 1);
            g[u][i] = '.';
            col[i] = dg[u + i] = udg[n - u + i] = false;
        }
    }
}
int main(void)
{
    cin >> n;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            g[i][j] = '.';
    dfs(0);
    return 0;
}