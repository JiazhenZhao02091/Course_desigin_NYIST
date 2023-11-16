import pymysql
from pyecharts import options as opts
from pyecharts.charts import Bar
from pyecharts.charts import Line
from pyecharts.components import Table
from pyecharts.charts import WordCloud
from pyecharts.charts import Pie
from pyecharts.charts import Funnel
from pyecharts.charts import Scatter
from pyecharts.charts import PictorialBar
from pyecharts.options import ComponentTitleOpts
from pyecharts.globals import SymbolType


# 获取 Mysql 的链接
conn = pymysql.connect(host='127.0.0.1'
                       , user='root'
                       , passwd='123456'
                       , port=3306
                       , db='bigdata'
                       , charset='utf8'
                       )
# 获取下标
cur = conn.cursor()


#5.画出美国死亡最多的10个州——>象柱状图
state = []
totalDeath = []
data = []

sql =  "SELECT state, SUM(deaths) AS total_deaths FROM us_covid WHERE date = '2020-05-19' GROUP BY state ORDER BY total_deaths DESC LIMIT 10;"
cur.execute(sql)
datas = cur.fetchall()
for x in datas:
    # data += x
    data.append(x)
print(data)
for n,v in data:
    state.append(n)
    totalDeath.append(v)



# sql = "select state from us_covid group by state order by sum(deaths) desc limit 10; "
# sql1 = "select sum(deaths) from us_covid group by state order by sum(deaths) desc limit 10; "
# cur.execute(sql)
# states = cur.fetchall()
# for x in states:
#     x = list(x)
#     state += x
# cur.execute(sql1)
# deaths = cur.fetchall()
# for x in deaths:
#     x = list(x)
#     totalDeaths += x


c = (
PictorialBar()
.add_xaxis(state)
.add_yaxis(
    "",
    totalDeath,
    label_opts=opts.LabelOpts(is_show=False),
    symbol_size=18,
    symbol_repeat="fixed",
    symbol_offset=[0, 0],
    is_symbol_clip=True,
    symbol=SymbolType.ROUND_RECT,
)
.reversal_axis()
.set_global_opts(
    title_opts=opts.TitleOpts(title="PictorialBar-美国各州死亡人数Top10"),
    xaxis_opts=opts.AxisOpts(is_show=False),
    yaxis_opts=opts.AxisOpts(
        axistick_opts=opts.AxisTickOpts(is_show=False),
        axisline_opts=opts.AxisLineOpts(
            linestyle_opts=opts.LineStyleOpts(opacity=0)
        ),
    ),
)
.render("F:\code\PythonView\\result5\\result.html")
)


# 关闭链接
cur.close()
conn.close()