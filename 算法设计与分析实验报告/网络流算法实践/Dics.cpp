#include <iostream>
#include <stdio.h>
#include <string.h>
#include <queue>
using namespace std;

const int maxn = 11000;
const int INF = 1 << 30;
int m, n, ecnt, s, t;

struct Edge
{
    int v, w, next;
};

struct Node
{
    int u, ecnt;
};

Edge edge[2 * maxn];
Node pre[maxn];
bool vis[maxn];
int head[maxn];
bool map[110][110];
int road[maxn];
int rcnt = 0;

void addEdge(int u, int v, int w)
{
    edge[ecnt].v = v;
    edge[ecnt].w = w;
    edge[ecnt].next = head[u];
    head[u] = ecnt++;
}

void init()
{
    ecnt = 0;
    s = 0;
    t = n + 1;
    memset(edge, 0, sizeof(edge));
    memset(head, -1, sizeof(head));
    for (int i = 1; i <= m; i++)
    {
        addEdge(0, i, 1);
        addEdge(i, 0, 0);
    }
    for (int i = m + 1; i <= n; i++)
    {
        addEdge(i, t, 1);
        addEdge(t, i, 0);
    }
}

bool bfs()
{
    memset(vis, 0, sizeof(vis));
    memset(pre, -1, sizeof(pre));
    pre[s].u = s;
    vis[s] = true;
    queue<int> q;
    q.push(s);
    while (!q.empty())
    {
        int u = q.front();
        q.pop();
        for (int i = head[u]; i + 1; i = edge[i].next)
        {
            int v = edge[i].v;
            if (!vis[v] && edge[i].w)
            {
                vis[v] = true;
                pre[v].u = u;
                pre[v].ecnt = i;
                if (v == t)
                {
                    return true;
                }
                q.push(v);
            }
        }
    }
    return false;
}

int EK()
{
    int ans = 0;
    while (bfs())
    {
        int mi = INF;
        for (int i = t; i != s; i = pre[i].u)
        {
            mi = min(mi, edge[pre[i].ecnt].w);
            if (i != t)
            {
                road[rcnt++] = i;
            }
        }
        for (int i = t; i != s; i = pre[i].u)
        {
            edge[pre[i].ecnt].w -= mi;
            edge[pre[i].ecnt ^ 1].w += mi;
        }
        ans += mi;
    }
    return ans;
}

int main()
{
    scanf("%d%d", &m, &n);
    init();
    int u, v;
    while (scanf("%d%d", &u, &v) != EOF)
    {
        if (u == -1 && v == -1)
        {
            break;
        }
        addEdge(u, v, 1);
        addEdge(v, u, 0);
        // map[u][v]=true;
    }
    int ans = EK();
    printf("%d\n", ans);
    memset(vis, 0, sizeof(vis));
    for (int i = rcnt - 1; i > 0; i -= 2)
    {
        if (ans == 0)
        {
            break;
        }
        if (!vis[road[i]])
        {
            ans--;
            vis[road[i]] = true;
            printf("%d %d\n", road[i], road[i - 1]);
        }
    }
    return 0;
}
// 一个有用的样例
// 5 10
// 3 8
// 3 7
// 1 7
//-1 -1
