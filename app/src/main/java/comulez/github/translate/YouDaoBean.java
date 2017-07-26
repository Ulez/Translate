package comulez.github.translate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public class YouDaoBean implements Parcelable {

    private String query;
    private String errorCode;
    private BasicBean basic;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    private String l;

    private List<WebBean> web;
    private List<String> translation;

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public static class BasicBean implements Parcelable {
        private String usphonetic;
        private String phonetic;
        private String ukphonetic;
        private List<String> explains;
        private String l;

        public String getUsphonetic() {
            return usphonetic;
        }

        public void setUsphonetic(String usphonetic) {
            this.usphonetic = usphonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkphonetic() {
            return ukphonetic;
        }

        public void setUkphonetic(String ukphonetic) {
            this.ukphonetic = ukphonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

        public String getL() {
            return l;
        }

        public void setL(String l) {
            this.l = l;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.usphonetic);
            dest.writeString(this.phonetic);
            dest.writeString(this.ukphonetic);
            dest.writeStringList(this.explains);
            dest.writeString(this.l);
        }

        public BasicBean() {
        }

        protected BasicBean(Parcel in) {
            this.usphonetic = in.readString();
            this.phonetic = in.readString();
            this.ukphonetic = in.readString();
            this.explains = in.createStringArrayList();
            this.l = in.readString();
        }

        public static final Creator<BasicBean> CREATOR = new Creator<BasicBean>() {
            @Override
            public BasicBean createFromParcel(Parcel source) {
                return new BasicBean(source);
            }

            @Override
            public BasicBean[] newArray(int size) {
                return new BasicBean[size];
            }
        };
    }

    public static class WebBean implements Parcelable {
        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + ":" + value + " ";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.key);
            dest.writeStringList(this.value);
        }

        public WebBean() {
        }

        protected WebBean(Parcel in) {
            this.key = in.readString();
            this.value = in.createStringArrayList();
        }

        public static final Creator<WebBean> CREATOR = new Creator<WebBean>() {
            @Override
            public WebBean createFromParcel(Parcel source) {
                return new WebBean(source);
            }

            @Override
            public WebBean[] newArray(int size) {
                return new WebBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.query);
        dest.writeString(this.errorCode);
        dest.writeParcelable(this.basic, flags);
        dest.writeString(this.l);
        dest.writeList(this.web);
        dest.writeStringList(this.translation);
    }

    @Override
    public String toString() {
        return "YouDaoBean{" +
                "query='" + query + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", basic=" + basic +
                ", l='" + l + '\'' +
                ", web=" + web +
                ", translation=" + translation +
                '}';
    }

    public YouDaoBean() {
    }

    protected YouDaoBean(Parcel in) {
        this.query = in.readString();
        this.errorCode = in.readString();
        this.basic = in.readParcelable(BasicBean.class.getClassLoader());
        this.l = in.readString();
        this.web = new ArrayList<WebBean>();
        in.readList(this.web, WebBean.class.getClassLoader());
        this.translation = in.createStringArrayList();
    }

    public static final Parcelable.Creator<YouDaoBean> CREATOR = new Parcelable.Creator<YouDaoBean>() {
        @Override
        public YouDaoBean createFromParcel(Parcel source) {
            return new YouDaoBean(source);
        }

        @Override
        public YouDaoBean[] newArray(int size) {
            return new YouDaoBean[size];
        }
    };
}
