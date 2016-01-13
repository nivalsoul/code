package com.nivalsoul.code.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class CollectionAction {

	public static void main(String[] args) {
		Random r = new Random();
		int m = 1000000;
		Set<String> A = new HashSet<>();
		for(int i=0;i<m;i++){
			int k = r.nextInt(m);
			A.add("CHUAN"+k);
		}
		Set<String> B = new HashSet<>();
		for(int i=0;i<m;i++){
			int k = r.nextInt(m);
			B.add("CHUAN"+k);
		}
		Set<String> C = new HashSet<>();
		for(int i=0;i<m;i++){
			int k = r.nextInt(m);
			C.add("CHUAN"+k);
		}
		Set<String> D = new HashSet<>();
		for(int i=0;i<m;i++){
			int k = r.nextInt(m);
			D.add("CHUAN"+k);
		}
		Map<String, Set<String>> sets = new HashMap<>();
		sets.put("A", A);
		sets.put("B", B);
		sets.put("C", C);
		sets.put("D", D);
		
		long start = new Date().getTime();
		
		Map<String, Set<String>> map = getReverseSortMap(sets);
		Map<Integer, List<String>> result = getMatchedCollection(map, 4);
		
		long end = new Date().getTime();
		
		System.out.println("total time: "+(end-start)+" ms");
		//System.out.println("4个集合都出现："+result.get(4));
		//System.out.println("3个集合都出现："+result.get(3));
		//System.out.println("2个集合都出现："+result.get(2));
	}

	/**
	 * 将给定集合生成倒排序列表：<br>
	 * 分别遍历A，B，C，D，将集合中每个值作为key，创建map，value则为集合名称的list
	 * @param sets 给定的集合，key为集合名称，value则为集合数据
	 * @return 倒排序列表
	 */
	public static Map<String, Set<String>> getReverseSortMap(Map<String, Set<String>> sets) {
		Map<String, Set<String>> result = new HashMap<>();
		// 遍历集合
		Iterator<Entry<String, Set<String>>> iter = sets.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Set<String>> entry = iter.next();
			String key = entry.getKey();// 集合名称
			Set<String> set = entry.getValue();// 集合value
			// 遍历每个集合，将集合中的值作为key，集合名称的列表作为value添加到map中，形成集合数据的倒排序列表
			for (String val : set) {
				Set<String> list = null;
				if (result.containsKey(val)) {// 如果key已经存在，则将集合名称添加到value的set中
					list = result.get(val);
					list.add(key);
				} else {
					list = new HashSet<>();
					list.add(key);
				}
				result.put(val, list);
			}
		}
		return result;
	}

	/**
	 * 遍历倒排序结果map：<br>
	 * 遍历这个map的value（list），如果list的size等于集合个数，则匹配N，<br>
	 * 如果size等于(集合个数-1)，则匹配N-1个集合的条件，完成后即可得出排序结果，同时也知道匹配情况是在哪些集合中。
	 * @param map
	 * @param n 集合个数
	 * @return 匹配列表map，key为匹配集合个数，value为相应的记录
	 */
	public static Map<Integer, List<String>> getMatchedCollection(Map<String, Set<String>> map, int n) {
		Map<Integer, List<String>> result = new HashMap<>();
		Iterator<Entry<String, Set<String>>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Set<String>> entry = iter.next();
			String key = entry.getKey();// 集合的值
			Set<String> set = entry.getValue();// 集合名称列表
			int k = n;
			while (k > 1) {
				if (k == set.size()) {
					List<String> list = null;
					if (result.containsKey(k)) {
						list = result.get(k);
						list.add(key);
					} else {
						list = new ArrayList<>();
						list.add(key);
					}
					result.put(k, list);
				}
				k--;
			}
		}
		return result;
	}

}
