import pymysql
import pandas as pd
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

# 数据处理
# 打开文本文件
with open('F:\code\PythonView\\result2\\new_cases.txt', 'r') as file:
    # 读取文件内容并以回车符进行分割
    cases = file.read().split('\n')
with open('F:\code\PythonView\\result2\\new_deaths.txt', 'r') as file:
    # 读取文件内容并以回车符进行分割
    deaths = file.read().split('\n')
# 打印列表数据
print(cases)
print(deaths)


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
sql = "select  distinct date from us_covid"
cur.execute(sql)
data = cur.fetchall()
date = []
for x in data:
    date += x
print(date)


#
(
Line(init_opts=opts.InitOpts(width="1600px", height="800px"))
.add_xaxis(xaxis_data=date)
.add_yaxis(
    series_name="新增确诊",
    y_axis=cases,
    markpoint_opts=opts.MarkPointOpts(
        data=[
            opts.MarkPointItem(type_="max", name="最大值")
        ]
    ),
    markline_opts=opts.MarkLineOpts(
        data=[opts.MarkLineItem(type_="average", name="平均值")]
    ),
)
.set_global_opts(
    title_opts=opts.TitleOpts(title="美国每日新增确诊折线图", subtitle=""),
    tooltip_opts=opts.TooltipOpts(trigger="axis"),
    toolbox_opts=opts.ToolboxOpts(is_show=True),
    xaxis_opts=opts.AxisOpts(type_="category", boundary_gap=False),
)
.render("F:\code\PythonView\\result2\\result1.html")
)
(
Line(init_opts=opts.InitOpts(width="1600px", height="800px"))
.add_xaxis(xaxis_data=date)
.add_yaxis(
    series_name="新增死亡",
    y_axis=deaths,
    markpoint_opts=opts.MarkPointOpts(
        data=[opts.MarkPointItem(type_="max", name="最大值")]
    ),
    markline_opts=opts.MarkLineOpts(
        data=[
            opts.MarkLineItem(type_="average", name="平均值"),
            opts.MarkLineItem(symbol="none", x="90%", y="max"),
            opts.MarkLineItem(symbol="circle", type_="max", name="最高点"),
        ]
    ),
)
.set_global_opts(
    title_opts=opts.TitleOpts(title="美国每日新增死亡折线图", subtitle=""),
    tooltip_opts=opts.TooltipOpts(trigger="axis"),
    toolbox_opts=opts.ToolboxOpts(is_show=True),
    xaxis_opts=opts.AxisOpts(type_="category", boundary_gap=False),
)
.render("F:\code\PythonView\\result2\\result2.html")
)



# 关闭链接
cur.close()
conn.close()