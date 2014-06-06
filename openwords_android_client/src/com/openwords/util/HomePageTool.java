
package com.openwords.util;
public class HomePageTool {

        private String name;
        private int id;
        
        public HomePageTool(String name, int id)
        {
                super();
                this.setName(name);
                this.setId(id);
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }
        
}