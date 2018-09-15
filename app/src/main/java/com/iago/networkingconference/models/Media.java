package com.iago.networkingconference.models;

import com.google.gson.annotations.SerializedName;

public class Media {

    private String type;

    private String label;

    private Files files;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Files getFiles() {
        return files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public class Files {

        @SerializedName("default")
        private String defaultURL;

        private Variations variations;

        public String getDefaultURL() {
            return defaultURL;
        }

        public void setDefaultURL(String defaultURL) {
            this.defaultURL = defaultURL;
        }

        public Variations getVariations() {
            return variations;
        }

        public void setVariations(Variations variations) {
            this.variations = variations;
        }

        public class Variations {

            private String small;

            private String original;

            private String tiny;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getTiny() {
                return tiny;
            }

            public void setTiny(String tiny) {
                this.tiny = tiny;
            }
        }
    }
}
