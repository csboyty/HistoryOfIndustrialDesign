package com.zhongyi.hid.dto;


import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "file")
public class HidBundle {

    @Element(name = "id")
    private String id;

    @Element(name = "name")
    private String name = "";

    @Element(name = "size")
    private String size = "";

    @Element(name = "url")
    private String url = "";

    @Element(name = "timestamp")
    private String timestamp;

    @Element(name = "md5")
    private String md5 = "";

    @Element(name = "op")
    private String op = "u";

    @Element(name = "info",required=false)
    private BundleInfo info;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public BundleInfo getInfo() {
        return info;
    }

    public void setInfo(BundleInfo info) {
        this.info = info;
    }

    @Root(name = "info")
    public static class BundleInfo {

    	 @ElementList(name = "genres", entry = "genre", required=false)
        private ArrayList<String> genre = new ArrayList<String>();

        //艺术家
        @ElementList(name = "artists", entry = "artist", required=false)
        private ArrayList<String> artists = new ArrayList<String>();

        //公司
        @ElementList(name = "organizations", entry = "organization", required=false)
        private ArrayList<String> organizations = new ArrayList<String>();

        //发生年份
        @Element(name = "year")
        private String year;

        //发生城市
        @Element(name ="city",required=false)
        private String city;

        //城市坐标
        @Element(name = "coordinate",required=false)
        private String coordinate = "-1,-1";

        @Element(name = "summary",required = false)
        private String summary;

        @Element(name = "postDate")
        private String postDate;
        
        @Element(name = "profile",required = false)
        private String profile;
        
        @Element(name = "background",required = false)
        private String background;
        

        public ArrayList<String> getGenre() {
			return genre;
		}

		public void setGenre(ArrayList<String> genre) {
			this.genre = genre;
		}

		public ArrayList<String> getArtists() {
            return artists;
        }

        public void setArtists(ArrayList<String> artists) {
            this.artists = artists;
        }

        public ArrayList<String> getOrganizations() {
            return organizations;
        }

        public void setOrganizations(ArrayList<String> organizations) {
            this.organizations = organizations;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(String coordinate) {
            this.coordinate = coordinate;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

		public String getPostDate() {
			return postDate;
		}

		public void setPostDate(String postDate) {
			this.postDate = postDate;
		}

		public String getProfile() {
			return profile;
		}

		public void setProfile(String profile) {
			this.profile = profile;
		}

		public String getBackground() {
			return background;
		}

		public void setBackground(String background) {
			this.background = background;
		}
		
        
        
    }







}
