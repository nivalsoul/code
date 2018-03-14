package com.nivalsoul.code;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MongoDBTest {
	
	static List<JSONObject> result = new ArrayList<JSONObject>();
    static Set<String> keys = new HashSet<String>();

	public static void main(String[] args) throws IOException {
		String dbName = "dbinfo";
        String collName = "tableinfo";
        String url = "mongodb://127.0.0.1:27017";
        MongoDBUtil.init(url);
        MongoDBUtil instance = new MongoDBUtil();
        MongoCollection<Document> coll = instance.getCollection(dbName, collName);
        
        File file = new File("G:\\table.json");
		String data = FileUtils.readFileToString(file);
        //System.out.println(data);
		JSONObject json = JSON.parseObject(data);
		int index=0;
		parseObj(json, index);
		
		System.out.println(result);
		for (JSONObject row : result) {
			String[] pNames = row.getString("pName").split(">>>");
			for(int i=pNames.length-1;i>1;i--) {
				row.put(pNames[i-1], pNames[i]);
			}
			row.remove("pName");
			for(String key : row.keySet()) {
				System.out.print(key+"\t");
			}
			Document document = Document.parse(row.toJSONString());
			coll.insertOne(document);
		}

	}
	
	private static void parseObj(JSONObject json, int index) {
		Set<String> keys0 = json.keySet();
		System.out.println("第"+index+"层：");
		boolean isLeaf = true;
		for (String key : keys0) {
			Object child = json.get(key);
			String pName = json.get("pName")+">>>"+key;
			if(child instanceof JSONObject) {
				isLeaf = false;
				JSONObject obj = json.getJSONObject(key);
				obj.put("pName", pName);
				parseObj(obj, index+1);
			}else if (child instanceof JSONArray) {
				isLeaf = false;
				JSONArray arr = json.getJSONArray(key);
				parseArr(arr, index+1, pName);
			}
		}
		if(isLeaf) {
			result.add(json);
			keys.addAll(json.keySet());
		}
	}
    
    private static void parseArr(JSONArray arr, int i, String pName) {
		for (int k=0;k<arr.size();k++) {
			Object a = arr.get(k);
			if(a instanceof JSONObject) {
				JSONObject obj = arr.getJSONObject(k);
				obj.put("pName", pName);
				parseObj(obj, i);
			}
		}
	}

}
