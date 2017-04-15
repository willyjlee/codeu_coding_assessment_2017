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

import java.util.Collection;
import java.util.HashMap;

final class MyJSON implements JSON {
    
    private HashMap<String, String>strs=new HashMap<>();  // map from string key to string value in object
    private HashMap<String, JSON>objs=new HashMap<>();    // map from string key to JSON value in object
    
    @Override
    public JSON getObject(String name) {
        return objs.get(name);  // get JSON object from map
    }
    
    @Override
    public JSON setObject(String name, JSON value) {
        objs.put(name, value);  // sets JSON object of the key name to be value
        return this;
    }
    
    @Override
    public String getString(String name) {
        return strs.get(name);  // get string given key name
    }
    
    @Override
    public JSON setString(String name, String value) {
        strs.put(name, value);  // sets string of the key name to be value
        return this;
    }
    
    @Override
    public void getObjects(Collection<String> names) {
        names.clear();
        names.addAll(objs.keySet());  // copies over all string keys of the objects
    }
    
    @Override
    public void getStrings(Collection<String> names) {
        names.clear();
        names.addAll(strs.keySet());  // copies over all string keys of the string values
    }
}
