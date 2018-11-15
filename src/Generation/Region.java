package Generation;

//TODO Направление ветра
public enum Region {
    BRE(24, 24, 2) {
        @Override
        public String toString() {
            return "Берсткая область";
        }
    },
    VIT(23, 23, 2) {
        @Override
        public String toString() {
            return "Витебская область";
        }
    },
    GRO(24, 24, 2.4) {
        @Override
        public String toString() {
            return "Гродненская область";
        }
    },
    GOM(25, 17, 1.9) {
        @Override
        public String toString() {
            return "Гомелькая область";
        }
    },
    MIN(23, 24, 2.9) {
        @Override
        public String toString() {
            return "Минская область";
        }
    },
    MOG(24, 24, 2.6) {
        @Override
        public String toString() {
            return "Могилевская область";
        }
    };


    private final double temp;
    private final double speedMax;
    private final double speedAv;

    public double getTemp() {
        return temp;
    }

    public double getSpeedMax() {
        return speedMax;
    }

    public double getSpeedAv() {
        return speedAv;
    }

    Region(double temp, double speedMax, double speedAv) {
        this.temp = temp;
        this.speedMax = speedMax;
        this.speedAv = speedAv;
    }
}
