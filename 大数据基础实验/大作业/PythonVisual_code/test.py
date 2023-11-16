import numpy as np
import xlrd
import pandas as pd


def read(file):
    wb = xlrd.open_workbook(filename=file)  # 打开文件
    sheet = wb.sheet_by_index(0)  # 通过索引获取表格
    rows = sheet.nrows  # 获取行数
    all_content = []  # 存放读取的数据
    for j in range(1, 10):  # 取第1~第9列对的数据
        temp = []
        for i in range(1, rows):
            cell = sheet.cell_value(i, j)  # 获取数据
            temp.append(cell)
        all_content.append(temp)  # 按列添加到结果集中
        temp = []
    return np.array(all_content)


def entropy(data0):
    # 返回每个样本的指数
    # 样本数，指标个数
    n, m = np.shape(data0)
    # 一列一个样本，一行一个指标
    # 下面是归一化
    maxium = np.max(data0, axis=1)
    minium = np.min(data0, axis=1)
    data1 = data0.copy()
    for i in range(0, 9):
        data0[i] = (data0[i] - minium[i]) * 1.0 / (maxium[i] - minium[i])
    ##计算第j项指标，第i个样本占该指标的比重
    sumzb = np.sum(data0, axis=1)
    for i in range(0, 9):
        data0[i] = data0[i] / sumzb[i]
    # 对ln0处理
    a = data0 * 1.0
    a[np.where(data0 == 0)] = 0.0001
    #    #计算每个指标的熵
    e = (-1.0 / np.log(m)) * np.sum(data0 * np.log(a), axis=1)
    #    #计算权重
    w = (1 - e) / np.sum(1 - e)
    print(w)
    return w


# 极小型指标 -> 极大型指标
def dataDirection_1(datas):
    return (np.max(datas) - datas)  # 套公式


# 中间型指标 -> 极大型指标
def dataDirection_2(datas, x_best):
    temp_datas = datas - x_best
    M = np.max(abs(temp_datas))
    answer_datas = 1 - abs(datas - x_best) / M  # 套公式
    return answer_datas


# 区间型指标 -> 极大型指标
def dataDirection_3(datas, x_min, x_max):
    M = max(x_min - np.min(datas), np.max(datas) - x_max)
    answer_list = []
    for i in datas:
        if (i < x_min):
            answer_list.append(1 - (x_min - i) / M)  # 套公式
        elif (x_min <= i <= x_max):
            answer_list.append(1)
        else:
            answer_list.append(1 - (i - x_max) / M)
    return np.array(answer_list)


def temp2(datas):
    K = np.power(np.sum(pow(datas, 2), axis=1), 0.5)
    for i in range(0, K.size):
        for j in range(0, datas[i].size):
            datas[i, j] = datas[i, j] / K[i]  # 套用矩阵标准化的公式
    return datas


def temp3(answer, w):
    list_max = []
    for i in answer:
        list_max.append(np.max(i[:]))  # 获取每一列的最大值
    list_max = np.array(list_max)
    list_min = []
    for i in answer:
        list_min.append(np.min(i[:]))  # 获取每一列的最小值
    list_min = np.array(list_min)
    max_list = []  # 存放第i个评价对象与最大值的距离
    min_list = []  # 存放第i个评价对象与最小值的距离
    answer_list = []  # 存放评价对象的未归一化得分
    for k in range(0, np.size(answer, axis=1)):  # 遍历每一列数据
        max_sum = 0
        min_sum = 0
        for q in range(0, 9):  # 有四个指标
            max_sum += w[q] * np.power(answer[q, k] - list_max[q], 2)  # 按每一列计算Di+
            min_sum += w[q] * np.power(answer[q, k] - list_min[q], 2)  # 按每一列计算Di-
        max_list.append(pow(max_sum, 0.5))
        min_list.append(pow(min_sum, 0.5))
        answer_list.append(min_list[k] / (min_list[k] + max_list[k]))  # 套用计算得分的公式 Si = (Di-) / ((Di+) +(Di-))
        max_sum = 0
        min_sum = 0
    answer = np.array(answer_list)  # 得分归一化
    return (answer / np.sum(answer))


def main():
    file = 'D:\\kong\\数学建模\\数据汇总.xlsx'
    answer1 = read(file)  # 读取文件
    answer2 = answer1.copy()
    for i in range(0, 9):  # 按照不同的列，根据不同的指标转换为极大型指标，因为只有四列
        if i == 3:  # 中间型指标
            answer1[i] = dataDirection_2(answer1[i], 0)
        elif i == 1 or i == 2 or i == 8:  # 极小型指标
            answer1[i] = dataDirection_1(answer1[i])
        else:  # 本来就是极大型指标，不用转换
            answer1[i] = answer1[i]
    answer3 = temp2(answer1)  # 正向数组标准化
    w = entropy(answer2)  # 计算权重
    answer4 = temp3(answer3, w)  # topsis
    data = pd.DataFrame(answer4)  # 计算得分
    print(data)
    # 将得分输出到excel表格中
    writer = pd.ExcelWriter('D:\\kong\\数学建模\\结果.xlsx')  # 写入Excel文件
    data.to_excel(writer, '得分', float_format='%.5f')  # ‘page_1’是写入excel的sheet名
    writer.save()
    writer.close()


if __name__ == '__main__':
    main()