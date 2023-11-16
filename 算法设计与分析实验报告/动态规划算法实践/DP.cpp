#include <iostream>
#include <cstring>
using namespace std;
const int N = 1024;
int c[N][N], b[N][N];
char s1[N], s2[N];
int len1, len2;
void LCS()
{
    for (int i = 1; i <= len1; i++)
    {
        for (int j = 1; j <= len2; j++)
        {
            if (s1[i - 1] == s2[j - 1])
            { // 注：此处的s1与s2序列是从s1[0]与s2[0]开始的
                c[i][j] = c[i - 1][j - 1] + 1;
                b[i][j] = 1;
            }
            else
            {
                if (c[i][j - 1] >= c[i - 1][j])
                {
                    c[i][j] = c[i][j - 1];
                    b[i][j] = 2;
                }
                else
                {
                    c[i][j] = c[i - 1][j];
                    b[i][j] = 3;
                }
            }
        }
    }
}

void LCS_PRINT(int i, int j)
{
    if (i == 0 || j == 0)
    {
        return;
    }
    if (b[i][j] == 1)
    {
        LCS_PRINT(i - 1, j - 1);
        cout << s1[i - 1];
    }
    else if (b[i][j] == 2)
    {
        LCS_PRINT(i, j - 1);
    }
    else
    {
        LCS_PRINT(i - 1, j);
    }
}

int main()
{
    cin >> s1;
    cin >> s2;
    len1 = strlen(s1);
    len2 = strlen(s2);
    for (int i = 0; i <= len1; i++)
    {
        c[i][0] = 0;
    }
    for (int j = 0; j <= len2; j++)
    {
        c[0][j] = 0;
    }
    LCS();
    cout << c[len1][len2] << endl;
    LCS_PRINT(len1, len2);
    return 0;
}

// 输入、输出
/*
ABCADAB
BACDBA
输出
4
BADB*/
