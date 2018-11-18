package Calcul.Calculations;

import Utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class Company {
    private String name;
    private XY xy;
    private int number;
    private LinkedList<Element> elements;
    private HashMap<XY, LinkedList<Element>> map;
    private double h;
    private double d;
    private double v1;
    private double tg;
    private double n3;
    private double tv;

    private double um;
    private double u;
    private double w0;
    private double delt;

    private double f;
    private double fe;
    private double vm;
    private double vm1;
    private double m;
    private double n;
    private double kF;
    private double xmhot;
    private double xmcold;
    private double udhot;
    private double udcold;
    private double rhot;
    private double rcold;
    private double xmihot;
    private double xmicold;


    public Company(String name, int number) {
        super();
        this.name = name;
        this.number = number;
    }


    public XY getXy() {
        return xy;
    }


    public void setXy(XY xy) {
        this.xy = xy;
    }


    public double getW0() {
        return w0;
    }


    public void setW0() {
        this.w0 = 4 * v1 / (Math.PI * Math.pow(d, 2));
    }

    public double getDelt() {
        return delt;
    }


    public void setDelt() {
        this.delt = tg - tv;
    }


    public double getF() {
        return f;
    }

    public void setF() {
        this.f = (1000 * Math.pow(w0, 2) * d) / (Math.pow(h, 2) * delt);
    }

    public double getFe() {
        return fe;
    }

    public void setFe() {
        this.fe = 800 * Math.pow(vm1, 3);
    }

    public double getVm() {
        return vm;
    }

    public void setVm() {
        this.vm = 0.65 * Math.pow(v1 * delt / h, 1 / 3);
    }

    public double getVm1() {
        return vm1;
    }

    public void setVm1() {
        this.vm1 = 1.3 * w0 * d / h;
    }


    public double getM() {
        return m;
    }

    public void setM() {
        this.m = 1 / (0.67 + 0.1 * Math.pow(f, 0.5) + 0.34 * Math.pow(f, 1 / 3));
    }


    public double getN(boolean hot) {
        if (hot == true) {
            if (vm >= 2) {
                n = 1;
            } else {
                if (vm >= 0.5 && vm < 2) {
                    n = 0.532 * Math.pow(vm, 2) - 2.13 * vm + 3.13;
                } else {
                    n = 4.4 * vm;
                }

            }
        } else {
            if (vm1 >= 2) {
                n = 1;
            } else {
                if (vm1 >= 0.5 && vm1 < 2) {
                    n = 0.532 * Math.pow(vm1, 2) - 2.13 * vm1 + 3.13;
                } else {
                    n = 4.4 * vm1;
                }
            }
        }

        return n;
    }


    public double getkF() {
        return kF;
    }

    public void setkF() {
        if (n3 >= 0.9) {
            this.kF = 2;
        } else {
            if (n3 > 0.75 && n3 < 0.9) {
                this.kF = 2.5;
            } else {
                this.kF = 3;
            }

        }

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public LinkedList<Element> getElements() {
        return elements;
    }


    public void setElements(LinkedList<Element> elements) {
        this.elements = elements;
    }


    public double getH() {
        return h;
    }


    public void setH(double h) {
        this.h = h;
    }


    public double getD() {
        return d;
    }


    public void setD(double d) {
        this.d = d;
    }


    public double getV1() {
        return v1;
    }


    public void setV1(double v1) {
        this.v1 = v1;
    }


    public double getTg() {
        return tg;
    }


    public void setTg(double tg) {
        this.tg = tg;
    }


    public double getN3() {
        return n3;
    }


    public void setN3(double n3) {
        this.n3 = n3;
    }


    public double getTv() {
        return tv;
    }


    public void setTv(double tv) {
        this.tv = tv;
    }


    public double getUm() {
        return um;
    }


    public void setUm(double um) {
        this.um = um;
    }


    public double getU() {
        return u;
    }


    public void setU(double u) {
        this.u = u;
    }


    public Element getElementByCode(String Code) {
        Element tmpel = null;
        for (Element e : elements) {
            if (e.getCode().equals(Code)) {
                tmpel = e;
                break;
            }
        }
        return tmpel;

    }


    public double calcd(boolean h) {
        double d = 0;
        if (h) {
            if (vm <= 0.5) {
                d = 2.48 * (1 + 0.28 * Math.pow(fe, 1 / 3));
            } else if (vm > 0.5 && vm <= 2) {
                d = 4.95 * vm * (1 + 0.28 * Math.pow(fe, 1 / 3));
            } else
                d = 7 * Math.sqrt(vm) * (1 + 0.28 * Math.pow(fe, 1 / 3));

        } else {
            if (vm <= 0.5) {
                d = 5.7;
            } else if (vm > 0.5 && vm <= 2) {
                d = 11.4 * vm;
            } else
                d = 16 * Math.sqrt(vm);


        }

        return d;

    }


    public double getXmhot() {
        return xmhot;
    }

    public void setXmhot() {
        this.xmhot = ((5 - kF) * calcd(true) * h) / 4;
    }

    public double getXmcold() {
        return xmcold;
    }

    public void setXmcold() {
        this.xmcold = ((5 - kF) * calcd(false) * h) / 4;
    }

    public double calcUd(boolean h) {
        double um = 0;
        if (h) {
            if (vm <= 0.5) {
                um = 0.5;
            } else if (vm > 0.5 && vm <= 2) {
                um = vm;
            } else
                um = vm * (1 + 0.12 * Math.sqrt(f));

        } else {
            if (vm1 <= 0.5) {
                um = 0.5;
            } else if (vm1 > 0.5 && vm1 <= 2) {
                um = vm1;
            } else
                um = 2.2 * Math.sqrt(vm1);


        }

        return um;

    }


    public double getUdhot() {
        return udhot;
    }

    public void setUdhot() {
        this.udhot = calcUd(true);
    }

    public double getUdcold() {
        return udcold;
    }

    public void setUdcold() {
        this.udcold = calcUd(false);
    }


    public double calcr(boolean h) {
        double r = 0;
        if (h) {
            if (u / udhot <= 1)
                r = 0.67 * (u / udhot) + 1.67 * Math.pow(u / udhot, 2) - 1.34 * Math.pow(u / udhot, 3);
            else
                r = (3 * u / udhot) / (2 * Math.pow(u / udhot, 2) - (u / udhot) + 2);
        } else {
            if (u / udcold <= 1)
                r = 0.67 * (u / udcold) + 1.67 * Math.pow(u / udcold, 2) - 1.34 * Math.pow(u / udcold, 3);
            else
                r = (3 * u / udcold) / (2 * Math.pow(u / udcold, 2) - (u / udcold) + 2);


        }

        return r;

    }


    public double getRhot() {
        return rhot;
    }

    public void setRhot() {
        this.rhot = calcr(true);
    }

    public double getRcold() {
        return rcold;
    }

    public void setRcold() {
        this.rcold = calcr(false);
    }


    public double calcp(boolean h) {
        double p = 0;
        if (h) {
            if (u / udhot <= 0.25)
                p = 3;
            else if ((u / udhot > 0.25) && (u / udhot) <= 1)
                p = 8.43 * Math.pow(1 - u / udhot, 5) + 1;
            else
                p = 0.32 * (u / udhot) + 0.68;

        } else {
            if (u / udcold <= 0.25)
                p = 3;
            else if ((u / udcold > 0.25) && (u / udcold) <= 1)
                p = 8.43 * Math.pow(1 - u / udcold, 5) + 1;
            else
                p = 0.32 * (u / udcold) + 0.68;
        }

        return p;

    }


    public double getXmihot() {
        return xmihot;
    }

    public void setXmihot() {
        this.xmihot = xmhot * calcp(true);
    }

    public double getXmicold() {
        return xmicold;
    }

    public void setXmicold() {
        this.xmicold = xmcold * calcp(false);
    }

    @Override
    public String toString() {
        return name;
    }

    public ArrayList<String> getListCodesOfelements() {
        ArrayList<String> list = new ArrayList<String>();
        for (Element element : getElements()) {
            list.add(element.getCode());
        }

        return list;
    }

    public ArrayList<String> checkElem(ArrayList<String> sumelem) {
        if (getListCodesOfelements().containsAll(sumelem))
            return sumelem;
        else
            return null;

    }


    public void setMapVect(ArrayList<Vector2d> vList){
        LinkedList<XY> xy = new LinkedList<>();
        for (Vector2d v: vList) {
            xy.add(new XY(v.x, v.y));
        }
        setMap(xy);
    }

    public void setMap(LinkedList<XY> xy) {
        map = new HashMap<>();

        for (XY t_xy : xy) {

            XY temp_xy = getXY(t_xy);
            System.out.println(temp_xy);
            LinkedList<Element> temm_el = new LinkedList<>();
            for (Element el : elements) {

                double devX = xDevByXm(temp_xy, el.isHot());
                double s1 = s1(devX);
                double ty = ty(temp_xy);
                double s2 = s2(ty);
                double c = s1 * el.getCm();
                double cp = s2 * c;
                temm_el.add(new Element(el.getCode(), c, cp, el.getCcc()));


            }
            //System.out.println(t_xy);
            map.put(t_xy, temm_el);
        }


    }


    public HashMap<XY, LinkedList<Element>> getMap() {
        return map;
    }


    public XY getXY(XY xy) {
        XY new_xy = new XY(Math.abs(xy.getX() - getXy().getX()), Math.abs(xy.getY() - getXy().getY()));

        return new_xy;
    }

    public double xDevByXm(XY xy, boolean hot) {
        if (hot) {
            return xy.getX() / getXmhot();
        } else {
            return xy.getX() / getXmcold();
        }

    }

    public double s1(double devX) {
        double s1 = 0;
        if (h >= 2 && h < 10 && devX < 1) {
            s1 = 0.125 * (10 - h) + 0.125 * (h - 2) * (3 * Math.pow(devX, 4) - 8 * Math.pow(devX, 3) + 6 * Math.pow(devX, 2));
        } else {

            if (devX <= 1) {
                s1 = 3 * Math.pow(devX, 4) - 8 * Math.pow(devX, 3) + 6 * Math.pow(devX, 2);
            } else if (devX > 1 && devX <= 8) {
                s1 = (1.13) / (0.13 * Math.pow(devX, 2) + 1);


            } else if (devX > 8 && kF <= 1.5) {
                s1 = devX / (3.58 * Math.pow(devX, 2) - 35.2 * devX + 120);

            } else if (devX > 8 && kF > 1.5) {
                s1 = 1 / (0.1 * Math.pow(devX, 2) + 2.47 * devX - 17.8);
            }
        }
        return s1;

    }

    public double ty(XY xy) {
        double ty = 0;
        if (u <= 5) {
            ty = u * Math.pow(xy.getY(), 2) / Math.pow(xy.getX(), 2);
        } else {
            ty = 5 * Math.pow(xy.getY(), 2) / Math.pow(xy.getX(), 2);

        }

        return ty;
    }

    public double s2(double ty) {
        return 1 / (1 + 5 * ty + 12.8 * Math.pow(ty, 2) + 17 * Math.pow(ty, 3) + 45.1 * Math.pow(ty, 4));
    }


    public void calculateConcentration() {
        //TODO validation?
        setW0();
        setDelt();
        setF();
        setVm();
        setVm1();
        setFe();
        setM();
        setkF();
        setXmhot();
        setXmcold();
        setUdhot();
        setUdcold();
        setRhot();
        setRcold();
        setXmihot();
        setXmicold();

        LinkedList<Element> elements = getElements();
        Iterator iterator = elements.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Element el = (Element) iterator.next();
            double n = getN(el.isHot());
            el.setCm(n, getM(), getkF(), getH(), getDelt(), getV1(), getD());
            el.setUz();
            el.setCmi(getRhot(), getRcold());


            elements.set(i, el);

            i++;
        }
        setElements(elements);
    }

}


