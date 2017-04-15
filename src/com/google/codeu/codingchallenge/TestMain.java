// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

final class TestMain {
    
    public static void main(String[] args) {
        
        final Tester tests = new Tester();
        
        tests.add("Empty Object", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ }");
                
                final Collection<String> strings = new HashSet<>();
                obj.getStrings(strings);
                
                Asserts.isEqual(strings.size(), 0);
                
                final Collection<String> objects = new HashSet<>();
                obj.getObjects(objects);
                
                Asserts.isEqual(objects.size(), 0);
            }
        });
        
        tests.add("String Value", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"name\":\"sam doe\" }");
                
                Asserts.isEqual("sam doe", obj.getString("name"));
            }
        });
        
        tests.add("Object Value", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" } }");
                
                final JSON nameObj = obj.getObject("name");
                
                Asserts.isNotNull(nameObj);
                Asserts.isEqual("sam", nameObj.getString("first"));
                Asserts.isEqual("doe", nameObj.getString("last"));
            }
        });
        
        tests.add("custom1", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{  \"bab\"   : { \"inner1\" : {\"inner2\" : { \"inner3\" : \"inner3val\" } } }   , \"schmab\"   : { \"inner\"  : \"innerval\" }    , \"string\"  : \"stringval\"}");
                
                final JSON nameObj = obj.getObject("name");
                
                Asserts.isEqual(obj.getObject("schmab").getString("inner"), "innerval");
                
                Asserts.isEqual(obj.getObject("bab").getObject("inner1").getObject("inner2").getString("inner3"), "inner3val");
                Asserts.isEqual(obj.getString("string"), "stringval");
                
            }
        });
        
        tests.add("custom2", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"key\" : \"val\"    \n}");
                Asserts.isEqual(obj.getString("key"), "val");
            }
        });
        
        tests.add("custom3", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{   \"\" : \"val\",\"bab\":\"schmab \\t  \\n \" }");
                
                Asserts.isEqual(obj.getString(""), "val");
                Asserts.isEqual(obj.getString("bab"),"schmab \\t  \\n ");
            }
        });
        
        tests.add("custom4", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \" \\\\ \" : \"val\"  }");
                
                Asserts.isEqual(obj.getString(" \\\\ "),"val");
            }
        });
        
        tests.add("custom5", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"a\": { \"b\":\"c\" } }");
                
                Asserts.isEqual(obj.getObject("a").getString("b"), "c");
            }
        });
        
        
        tests.add("custom6", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{  \"hi\": { \"first\": \"sam\", \"last\": \"doe\" } }");
                
                Asserts.isEqual(obj.getObject("hi").getString("first"),"sam");
                Asserts.isEqual(obj.getObject("hi").getString("last"),"doe");
            }
        });
        
        tests.add("custom7", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"+\" : {\"-\":\"*\"} , \"bab\" : \"schmab\" }");
                
                Asserts.isEqual(obj.getObject("+").getString("-"),"*");
                Asserts.isEqual(obj.getString("bab"),"schmab");
            }
        });
        
        tests.add("custom8", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"\": \"val1\" , \"abc\" : \"val2\" ,  \"\\\"a\\tb\\nc\\\"\" : \"val3\" }");
                
                Asserts.isEqual(obj.getString(""),"val1");
                Asserts.isEqual(obj.getString("abc"), "val2");
                Asserts.isEqual(obj.getString("\\\"a\\tb\\nc\\\""),"val3");
            }
        });
        
        tests.add("custom9", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"{ \\\"name\\\" : \\\"val\\\"}\" : \"realval\"    }");
                
                Asserts.isEqual(obj.getString("{ \\\"name\\\" : \\\"val\\\"}"),"realval");
                
            }
        });
        
        tests.add("custom10", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"a\" : {\"b\" : { \"c\" : { \"d\" : { \"e\" : { \"f\" : { \"g\" : \"h\" } } } } }}  , \"a\": \"str\" }");
                
                Asserts.isEqual(obj.getObject("a").getObject("b").getObject("c").getObject("d")
                                .getObject("e").getObject("f").getString("g"),"h");
                Asserts.isEqual(obj.getString("a"), "str");
                
            }
        });
        
        tests.add("custom11", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"obj\":{\"a\":\"b\"} , \"str\" : \"strval\" }");
                
                Asserts.isEqual(obj.getObject("obj").getString("a"),"b");
                Asserts.isEqual(obj.getString("str"),"strval");
                
                obj.setString("obj", "objstr");
                Asserts.isEqual(obj.getObject("obj").getString("a"),"b");
                Asserts.isEqual(obj.getString("str"),"strval");
                Asserts.isEqual(obj.getString("obj"), "objstr");
                
                JSON x = new MyJSON();
                x.setString("xkey", "xval");
                
                obj.setObject("obj", x);
                Asserts.isEqual(obj.getObject("obj").getString("xkey"), "xval");
                
                ArrayList<String>objnames=new ArrayList<>();
                obj.getObjects(objnames);
                
                ArrayList<String>strnames=new ArrayList<>();
                obj.getStrings(strnames);
                
                Asserts.isEqual(objnames.size(), 1);
                Asserts.isEqual(strnames.size(), 2);
                
                obj.getObject("obj").getObjects(objnames);
                obj.getObject("obj").getStrings(strnames);
                
                Asserts.isEqual(objnames.size(),0);
                Asserts.isEqual(strnames.size(), 1);
                
                Asserts.isEqual(strnames.get(0), "xkey");
                Asserts.isEqual(obj.getObject("obj").getString(strnames.get(0)), "xval");
                
            }
        });
        
        tests.add("custom12", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{  }");
                
                Asserts.isEqual(obj.getString("hi"),null);
                
                obj.setString("hi","hival").setString("hi", "hival2").setString("bye","byeval");
                Asserts.isEqual(obj.getString("hi"),"hival2");
                Asserts.isEqual(obj.getString("bye"),"byeval");
            }
        });
        
        tests.add("custom13", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"part1\":   \n\n\n\n\n\n  {\"name\":\"a\"} ,    \"part2\":{\"name\":{\"age\":\"12\"}}}");
                final JSON obj1 = obj.getObject("part1");
                final JSON obj2 = obj.getObject("part2");
                
                Asserts.isEqual("a", obj1.getString("name"));
                Asserts.isEqual("12", obj2.getObject("name").getString("age"));
            }
        });
        
        tests.add("custom14", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"hi\": \"b\\\\a\\n\" , \"yo\": \"yo\"}");
                
                Asserts.isEqual(obj.getString("hi"), "b\\\\a\\n");
                Asserts.isEqual(obj.getString("yo"),"yo");
            }
        });
        
        tests.add("custom15", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"key\":\"value\" , \"key\" : {}}");
                
                JSON get=obj.getObject("key");
                
            }
        });
        
        tests.add("custom16", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ \"k1\":\"v1\" ,\"k2\": \"v2\" }");
                
                JSON get=obj.getObject("key");
                Asserts.isEqual(get, null);
                ArrayList<String>adds=new ArrayList<>();
                obj.getStrings(adds);
                Asserts.isEqual(adds.size(), 2);
                Asserts.isEqual(true, adds.contains("k1"));
                Asserts.isEqual(true, adds.contains("k2"));
            }
        });
        
        tests.add("custom17", new Test() {
            @Override
            public void run(JSONFactory factory) throws Exception {
                final JSONParser parser = factory.parser();
                final JSON obj = parser.parse("{ }");
                
            }
        });
        
        tests.run(new JSONFactory(){
            @Override
            public JSONParser parser() {
                return new MyJSONParser();
            }
            
            @Override
            public JSON object() {
                return new MyJSON();
            }
        });
    }
}
