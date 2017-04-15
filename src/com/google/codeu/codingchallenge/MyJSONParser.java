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


import java.io.IOException;

final class MyJSONParser implements JSONParser {
    
    // advance index in string until at character t. Error when there is a non whitespace character before meeting t
    public int adv(int ind, String s, char t) throws IOException{
        for(int i=ind;i<s.length();i++){
            if(s.charAt(i)!=t && !Character.isWhitespace(s.charAt(i)))
                throw new IOException();
            if(s.charAt(i)==t)
                return i;
        }
        throw new IOException();
    }
    
    // get the next non whitespace character after or at index ind
    public int get(int ind, String s) throws IOException{//
        for(int i=ind;i<s.length();i++){
            if(!Character.isWhitespace(s.charAt(i)))
                return i;
        }
        return -1;
    }
    
    // check for special characters in string that need be escaped with a backslash
    public boolean escgood(int ind, String s){
        return ind+1<s.length() && (s.charAt(ind+1)=='t' || s.charAt(ind+1)=='n' || s.charAt(ind+1)=='\\'
                                    || s.charAt(ind+1)=='"');
    }
    
    // visit through a string, throwing any errors and returning index ending the string
    public int pstr(int ind, String s) throws IOException{ // ind starting at first quote
        int i=ind+1;
        while(i<s.length()) {
            if (s.charAt(i) == '\\') {
                if (!escgood(i, s))
                    throw new IOException();
                else {
                    i+=2;
                    continue;
                }
            }
            else if(s.charAt(i)=='"'){
                return i;
            }
            else{
                i++;
            }
        }
        throw new IOException();
    }
    
    // recursively process the string and set contained strings or objects in the fill object.
    public int rec(String s, int ind, JSON fill) throws IOException{
        
        if(s.length()-ind<2)    // check for length being too small
            throw new IOException();
        if(s.charAt(ind)!='{')  // check for correct start of {
            throw new IOException();
        
        int i=ind+1;
        
        boolean prevc=false;    //comma
        boolean st=true;
        while(i<s.length()){
            int fbeg=get(i,s);
            if(fbeg==-1)
                throw new IOException();
            char fbegc=s.charAt(fbeg);
            
            String k;
            if(fbegc=='}') {    // possibly at end of string
                if(!st&&prevc)  // isn't the start of the string and there was a comma means syntax problem
                    throw new IOException();
                return fbeg;
            }
            else if(fbegc=='"'){    // possibly start of a string
                if(!st&&!prevc)
                    throw new IOException();
                i=fbeg;
                int sbeg=i;
                int send=pstr(sbeg,s);  // go through the string
                k=s.substring(sbeg+1, send);
                i=send+1;
            }else{
                throw new IOException();
            }
            st=false;
            
            //colon
            i=adv(i,s,':')+1;
            int vbeg=get(i,s);
            if(vbeg==-1 || (s.charAt(vbeg)!='"'&&s.charAt(vbeg)!='{'))
                throw new IOException();
            char vbegc=s.charAt(vbeg);
            if(vbegc=='"'){ // possibly start of the value part of a string
                i=vbeg;
                int svbeg=i;
                int svend=pstr(svbeg,s);
                String v=s.substring(svbeg+1,svend);
                i=svend+1;
                fill.setString(k,v);    // set the object fill's string key and value
            }else if(vbegc=='{'){   // possibly start of an object
                i=vbeg;
                MyJSON newf=new MyJSON();   // newf will be an object within the fill object
                i=rec(s,i,newf)+1;  // finish setting members of the newf object
                fill.setObject(k,newf); // add newf to be a contained object of fill
            }else{
                throw new IOException();
            }
            //comma
            int flast=get(i,s);
            if(flast==-1)
                throw new IOException();
            char flastc=s.charAt(flast);
            if(flastc=='}'){    // possibly end of the string
                prevc=false;    // no comma
            }
            else if(flastc==','){   // comma found
                prevc=true;
                i=flast;
                i++;
            }else
                throw new IOException();
        }
        throw new IOException();
        
    }
    
    @Override
    public JSON parse(String in) throws IOException {
        
        MyJSON r= new MyJSON(); // starting JSON object
        int l=rec(in, 0, r);    // fill in object r and return last index in string of that object used
        if(l!=in.length()-1)    // the whole string must be used because objects must end with }
            throw new IOException();
        return r;
    }
}
