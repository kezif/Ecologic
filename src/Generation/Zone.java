package Generation;

public enum Zone {
      DIRT {
        @Override
        public String toString() {
            return "Нет города лол";
        }
    }, HOUSE {
        @Override
        public String toString() {
            return "Жилой район";
        }
    }, SHOP {
        @Override
        public String toString() {
            return "Коммерченский район";
        }
    }, FACTORY{
        @Override
        public String toString() {
            return "Индустриальный район";
        }
    };


}
