package pd;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class XML<K>
{

    int size;       //Liczba zmiennych
    int num;
    int count = 0;  //Liczba równan
    int varcount;   //Liczba warunków
    String[][] tab;
    String[] signs;
    String[] vectb;
    String[] vars;
    String[] vartypes;
    String[] target;
    String to;

    XML()
    {
    }

    public void Show_Matrix(K[][] tab)
    {
        /*System.out.println(tab.length);
        System.out.println(tab[0].length);*/
        for (K[] element : tab)
        {
            for (int j = 0; j < element.length; j++)
            {
                if (j != (element.length - 1))
                {
                    System.out.print(element[j] + " ");
                }
                else
                {
                    System.out.println(element[j]);
                }
            }
        }
    }

    public void Show_Matrix(K[] tab)
    {
        for (int j = 0; j < tab.length; j++)
        {
            if (j != (tab.length - 1))
            {
                System.out.print(tab[j] + " ");
            }
            else
            {
                System.out.println(tab[j]);
            }
        }
    }

    public void WczytajXML(String plik)
    {
        try
        {
            File fxmlFile = new File(plik);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fxmlFile);
            doc.getDocumentElement().normalize();
            size = Integer.parseInt(doc.getElementsByTagName("size").item(0).getTextContent());
            num = Integer.parseInt(doc.getElementsByTagName("num").item(0).getTextContent());
            varcount = Integer.parseInt(doc.getElementsByTagName("count").item(0).getTextContent());
            tab = new String[num][size];
            vars = new String[varcount];
            target = new String[size];
            vartypes = new String[varcount];
            NodeList nList = doc.getElementsByTagName("war0");
            int i = 0;
            while (true)
            {
                if (i == 0)
                {
                    int j = 1;
                    while (true)
                    {
                        if (nList.item(0).getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElement = (Element) nList.item(0);
                            if (eElement.getElementsByTagName("w" + j).item(0) == null)
                            {
                                break;
                            }
                            vars[j - 1] = eElement.getElementsByTagName("w" + j).item(0).getTextContent();
                            vartypes[j - 1] = eElement.getElementsByTagName("typ" + j).item(0).getTextContent();
                            j++;
                        }
                    }
                    nList = doc.getElementsByTagName("war1");
                    i++;
                    continue;
                }
                if (nList.item(0).getNodeType() == Node.ELEMENT_NODE)
                {
                    count++;
                    Element eElement = (Element) nList.item(0);
                    int j = 1;
                    while (true)
                    {
                        if (eElement.getElementsByTagName("w" + j).item(0) == null)
                        {
                            break;
                        }
                        tab[i - 1][j - 1] = eElement.getElementsByTagName("w" + j).item(0).getTextContent();
                        /*System.out.println(eElement.getElementsByTagName("w" + j).item(0).getTextContent());*/
                        j++;
                    }
                }
                i++;
                nList = doc.getElementsByTagName("war" + i);
                if (nList.item(0) == null)
                {
                    break;
                }
                else
                {
                    continue;
                }
            }
            signs = new String[count];
            vectb = new String[count];
            i = 0;
            while (true)
            {
                if (i == 0)
                {
                    nList = doc.getElementsByTagName("war1");
                    i++;
                    continue;
                }
                if (nList.item(0).getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nList.item(0);
                    signs[i - 1] = eElement.getElementsByTagName("typ").item(0).getTextContent();
                    vectb[i - 1] = eElement.getElementsByTagName("wr").item(0).getTextContent();
                }
                i++;
                nList = doc.getElementsByTagName("war" + i);
                if (nList.item(0) == null)
                {
                    break;
                }
                else
                {
                    continue;
                }
            }
            nList = doc.getElementsByTagName("cel");
            if (nList.item(0).getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) nList.item(0);
                int j = 1;
                to = eElement.getElementsByTagName("to").item(0).getTextContent();
                while (true)
                {
                    if (eElement.getElementsByTagName("w" + j).item(0) == null)
                    {
                        break;
                    }
                    target[j - 1] = eElement.getElementsByTagName("w" + j).item(0).getTextContent();
                    j++;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e);
            e.printStackTrace();
        }
    }

    private void showPP()
    {
        System.out.println("Problem Pierwotny:\n");
        for (int k = 0; k < count; k++)
        {
            for (int l = 0; l < size; l++)
            {
                if (l != size - 1)
                {
                    System.out.print(tab[k][l] + "x" + (l + 1) + " + ");
                }
                else
                {
                    System.out.print(tab[k][l] + "x" + (l + 1) + " ");
                    if ("mr".equals(signs[k]))
                    {
                        System.out.print("<=");
                    }
                    else if ("wr".equals(signs[k]))
                    {
                        System.out.print(">=");
                    }
                    else if ("r".equals(signs[k]))
                    {
                        System.out.print("=");
                    }
                    else
                    {
                        System.out.print("unknown sign");
                    }
                }
            }
            System.out.println(" " + vectb[k]);
        }
        System.out.println();
        for (int i = 0; i < varcount; i++)
        {
            System.out.print("x" + (i + 1) + " ");
            if ("mr".equals(vartypes[i]))
            {
                System.out.print("<=");
            }
            else if ("wr".equals(vartypes[i]))
            {
                System.out.print(">=");
            }
            else if ("r".equals(vartypes[i]))
            {
                System.out.print("=");
            }
            else if ("d".equals(vartypes[i]))
            {
                System.out.print("dowolne\n");
                continue;
            }
            else
            {
                System.out.print("unknown sign");
            }
            System.out.println(" " + vars[i]);
        }
        System.out.println();
        System.out.print("f(");
        for (int i = 0; i < size; i++)
        {
            if (i != (size - 1))
            {
                System.out.print("x" + (i + 1) + ", ");
            }
            else
            {
                System.out.print("x" + (i + 1) + ") = ");
            }
        }
        for (int i = 0; i < target.length; i++)
        {
            if (i != (size - 1))
            {
                System.out.print(target[i] + "x" + (i + 1) + " + ");
            }
            else
            {
                System.out.print(target[i] + "x" + (i + 1) + " ");
            }
        }
        System.out.println("--> " + to);
    }

    private void showPD()
    {
        System.out.println("Problem Dualny:\n");
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < num; j++)
            {
                if (j != (num - 1))
                {
                    System.out.print(tab[j][i] + "y" + (j + 1) + " + ");
                }
                else
                {
                    System.out.print(tab[j][i] + "y" + (j + 1) + " ");
                }
            }
            if ("mr".equals(vartypes[i]))
            {
                System.out.print("<=");
            }
            else if ("wr".equals(vartypes[i]))
            {
                System.out.print(">=");
            }
            else if ("d".equals(vartypes[i]))
            {
                System.out.print("=");
            }
            else
            {
                System.out.print("unknown sign");
            }
            System.out.println(" " + target[i]);
        }
        System.out.println();
        for (int i = 0; i < count; i++)
        {
            System.out.print("y" + (i + 1) + " ");
            if ("mr".equals(signs[i]))
            {
                System.out.print(">=");
            }
            else if ("wr".equals(signs[i]))
            {
                System.out.print("<=");
            }
            else if ("r".equals(signs[i]))
            {
                System.out.print("dowolne\n");
                continue;
            }
            else
            {
                System.out.print("unknown sign");
            }
            System.out.println(" " + vars[i]);
        }
        System.out.println();
        System.out.print("g(");
        for (int i = 0; i < count; i++)
        {
            if (i != (count - 1))
            {
                System.out.print("y" + (i + 1) + ", ");
            }
            else
            {
                System.out.print("y" + (i + 1) + ") = ");
            }
        }
        for (int i = 0; i < count; i++)
        {
            if (i != (count - 1))
            {
                System.out.print(vectb[i] + "y" + (i + 1) + " + ");
            }
            else
            {
                System.out.print(vectb[i] + "y" + (i + 1) + " ");
            }
        }
        if ("max".equals(to))
        {
            System.out.println("--> min");
        }
        else
        {
            System.out.println("--> max");
        }
        System.out.println();
    }

    public String Rozwiaz()
    {
        String ret = new String();
        if (size == 2)
        {
            showPP();
            System.out.println();
            int liczba = size - 1;
            int tmp = liczba;
            while (tmp > 1)
            {
                tmp--;
                liczba = liczba * tmp;
            }
            Double[][] punkty = new Double[liczba][2];            
            double[] return_points = new double[2];
            double wynik;
            double x, y;
            Double[] highest_x = new Double[2];
            Double[] highest_y = new Double[2];
            highest_x[0] = (double) 0;
            highest_x[1] = (double) 0;
            highest_y[0] = (double) 0;
            highest_y[1] = (double) 0;
            System.out.println("Rozwiązywanie przy pomocy programu podstawowego.");
            ret+="Rozwiązywanie przy pomocy programu podstawowego.";
            Show_Matrix((K[][]) tab);
            tmp = 0;
            for (int i = 0; i < size; i++)
            {
                if (i != (size - 1))
                {
                    for (int j = i + 1; j < size; j++)
                    {
                        x = ((Double.parseDouble(vectb[i])) * (Double.parseDouble(tab[j][num - 1]))) - ((Double.parseDouble(vectb[j])) * (Double.parseDouble(tab[i][num - 1])));
                        x = x / ((Double.parseDouble(tab[i][num - 2])) * (Double.parseDouble(tab[j][num - 1])) - (Double.parseDouble(tab[j][num - 2])) * (Double.parseDouble(tab[i][num - 1])));
                        y = ((Double.parseDouble(vectb[j])) * (Double.parseDouble(tab[i][num - 2]))) - ((Double.parseDouble(vectb[i])) * (Double.parseDouble(tab[j][num - 2])));
                        y = y / ((Double.parseDouble(tab[i][num - 2])) * (Double.parseDouble(tab[j][num - 1])) - (Double.parseDouble(tab[j][num - 2])) * (Double.parseDouble(tab[i][num - 1])));
                        punkty[tmp][0] = x;
                        punkty[tmp][1] = y;
                        tmp++;
                        System.out.println(x + " " + y);
                    }
                }
                double check_x = (Double.parseDouble(vectb[i])) / (Double.parseDouble(tab[i][num - 2]));
                double check_y = (Double.parseDouble(vectb[i])) / (Double.parseDouble(tab[i][num - 1]));
                if (check_x > highest_x[1])
                {
                    highest_x[0] = (double) i;
                    highest_x[1] = check_x;
                }
                if (check_y > highest_y[1])
                {
                    highest_y[0] = (double) i;
                    highest_y[1] = check_y;
                }
            }
            double start = (double) highest_x[1];
            int prosta = (int) (double) highest_x[0];
            wynik = highest_x[1] * Double.parseDouble(target[0]);
            while (true)
            {
                int tmp1;
                for (int i = 0; i < punkty.length; i++)
                {
                    if (punkty[i][0] < start)
                    {
                        if ((Double.parseDouble(tab[prosta][0]) * punkty[i][0] + Double.parseDouble(tab[prosta][1]) * punkty[i][1]) == Double.parseDouble(vectb[prosta]))
                        {
                            if (((Double.parseDouble(target[0]) * punkty[i][0] + Double.parseDouble(target[1]) * punkty[i][1] >= wynik) && ("max".equals(to))) || ((Double.parseDouble(target[0]) * punkty[i][0] + Double.parseDouble(target[1]) * punkty[i][1] <= wynik) && ("min".equals(to))))
                            {
                                boolean check = true;
                                for (int k = 0; k < 2; k++)
                                {
                                    if (vartypes[k].equals("mr"))
                                    {
                                        if (punkty[i][k] > Double.parseDouble(vars[k]))
                                        {
                                            check = false;
                                            break;
                                        }
                                    }
                                    if (vartypes[k].equals("wr"))
                                    {
                                        if (punkty[i][k] < Double.parseDouble(vars[k]))
                                        {
                                            check = false;
                                            break;
                                        }
                                    }
                                    if (vartypes[k].equals("r"))
                                    {
                                        if (punkty[i][k] != Double.parseDouble(vars[k]))
                                        {
                                            check = false;
                                            break;
                                        }
                                    }
                                }
                                if (check == true)
                                {
                                    wynik = Double.parseDouble(target[0]) * punkty[i][0] + Double.parseDouble(target[1]) * punkty[i][1];
                                    start = (int) (double) punkty[i][0];
                                    return_points[0] = (int) (double) punkty[i][0];
                                    return_points[1] = (int) (double) punkty[i][1];
                                    for (int j = 0; j < size; j++)
                                    {
                                        if ((Double.parseDouble(tab[j][0]) * punkty[i][0] + Double.parseDouble(tab[j][1]) * punkty[i][1]) == Double.parseDouble(vectb[j]))
                                        {
                                            if (prosta != j)
                                            {
                                                prosta = j;
                                                break;
                                            }
                                        }
                                    }
                                }
                                continue;
                            }
                        }
                    }
                }
                break;
            }
            if (((Double.parseDouble(target[1]) * (double) highest_y[1]) >= wynik) && ("max".equals(to)))
            {
                if (vartypes[1].equals("mr"))
                {
                    if ((double) highest_y[1] <= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
                    }
                }
                if (vartypes[1].equals("wr"))
                {
                    if ((double) highest_y[1] >= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
                    }
                }
                if (vartypes[1].equals("r"))
                {
                    if ((double) highest_y[1] == Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
                    }
                }
                //wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
            }
            if (((Double.parseDouble(target[1]) * (double) highest_y[1]) <= wynik) && ("min".equals(to)))
            {
                if (vartypes[1].equals("mr"))
                {
                    if ((double) highest_y[1] <= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
                    }
                }
                if (vartypes[1].equals("wr"))
                {
                    if ((double) highest_y[1] >= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
                    }
                }
                if (vartypes[1].equals("r"))
                {
                    if ((double) highest_y[1] == Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(target[1]) * (double) highest_y[1];
                    }
                }
                //wynik = Double.parseDouble(target[1])*(double)highest_y[1];
            }
            System.out.println("\nWynik: " + wynik + " dla punktów: x1 = " + return_points[0] + " i x2 = " + return_points[1] + ".\n");
            ret+="\nWynik: " + wynik + " dla punktów: x1 = " + return_points[0] + " i x2 = " + return_points[1] + ".\n";
            return ret;
        }
        if (num == 2)
        {
            showPP();
            System.out.println();
            showPD();
            int liczba = size - 1;
            int tmp = liczba;
            while (tmp > 1)
            {
                tmp--;
                liczba = liczba * tmp;
            }
            Double[][] punkty = new Double[liczba][2];
            double[] return_points = new double[2];
            double wynik;
            double x, y;
            int lines[] = new int[2];
            Double[] highest_x = new Double[2];
            Double[] highest_y = new Double[2];
            highest_x[0] = (double) 0;
            highest_x[1] = (double) 0;
            highest_y[0] = (double) 0;
            highest_y[1] = (double) 0;
            System.out.println("Rozwiązywanie przy pomocy programu dualnego.");
            ret+="Rozwiązywanie przy pomocy programu dualnego.";
            tmp = 0;
            for (int i = 0; i < size; i++)
            {
                if (i != (size - 1))
                {
                    for (int j = i + 1; j < size; j++)
                    {
                        x = ((Double.parseDouble(target[i])) * (Double.parseDouble(tab[num - 1][j]))) - ((Double.parseDouble(target[j])) * (Double.parseDouble(tab[num - 1][i])));
                        x = x / ((Double.parseDouble(tab[num - 2][i])) * (Double.parseDouble(tab[num - 1][j])) - (Double.parseDouble(tab[num - 2][j])) * (Double.parseDouble(tab[num - 1][i])));
                        y = ((Double.parseDouble(target[j])) * (Double.parseDouble(tab[num - 2][i]))) - ((Double.parseDouble(target[i])) * (Double.parseDouble(tab[num - 2][j])));
                        y = y / ((Double.parseDouble(tab[num - 2][i])) * (Double.parseDouble(tab[num - 1][j])) - (Double.parseDouble(tab[num - 2][j])) * (Double.parseDouble(tab[num - 1][i])));
                        punkty[tmp][0] = x;
                        punkty[tmp][1] = y;
                        tmp++;
                    }
                }
                double check_x = (Double.parseDouble(target[i])) / (Double.parseDouble(tab[num - 2][i]));
                double check_y = (Double.parseDouble(target[i])) / (Double.parseDouble(tab[num - 1][i]));
                if (check_x > highest_x[1])
                {
                    highest_x[0] = (double) i;
                    highest_x[1] = check_x;
                }
                if (check_y > highest_y[1])
                {
                    highest_y[0] = (double) i;
                    highest_y[1] = check_y;
                }
            }
            double start = (double) highest_x[1];
            int prosta = (int) (double) highest_x[0];
            wynik = highest_x[1] * Double.parseDouble(vectb[0]);
            while (true)
            {
                int tmp1;
                for (int i = 0; i < punkty.length; i++)
                {
                    if (punkty[i][0] < start)
                    {
                        if ((Double.parseDouble(tab[0][prosta]) * punkty[i][0] + Double.parseDouble(tab[1][prosta]) * punkty[i][1]) == Double.parseDouble(target[prosta]))
                        {
                            if (((Double.parseDouble(vectb[0]) * punkty[i][0] + Double.parseDouble(vectb[1]) * punkty[i][1] <= wynik) && ("max".equals(to))) || ((Double.parseDouble(vectb[0]) * punkty[i][0] + Double.parseDouble(vectb[1]) * punkty[i][1] >= wynik) && ("min".equals(to))))
                            {
                                boolean check = true;
                                for (int k = 0; k < 2; k++)
                                {
                                    if (vartypes[k].equals("mr"))
                                    {
                                        if (punkty[i][k] > Double.parseDouble(vars[k]))
                                        {
                                            check = false;
                                            break;
                                        }
                                    }
                                    if (vartypes[k].equals("wr"))
                                    {
                                        if (punkty[i][k] < Double.parseDouble(vars[k]))
                                        {
                                            check = false;
                                            break;
                                        }
                                    }
                                }
                                if (check == true)
                                {
                                    wynik = Double.parseDouble(vectb[0]) * punkty[i][0] + Double.parseDouble(vectb[1]) * punkty[i][1];
                                    start = (int) (double) punkty[i][0];
                                    return_points[0] = (int) (double) punkty[i][0];
                                    return_points[1] = (int) (double) punkty[i][1];
                                    lines[0] = prosta;
                                    for (int j = 0; j < size; j++)
                                    {
                                        if ((Double.parseDouble(tab[0][j]) * punkty[i][0] + Double.parseDouble(tab[1][j]) * punkty[i][1]) == Double.parseDouble(target[j]))
                                        {
                                            if (prosta != j)
                                            {
                                                prosta = j;
                                                lines[1] = prosta;
                                                break;
                                            }
                                        }
                                    }
                                }
                                continue;
                            }
                        }
                    }
                }
                break;
            }
            if (((Double.parseDouble(vectb[1]) * (double) highest_y[1]) <= wynik) && ("max".equals(to)))
            {
                if (vartypes[1].equals("mr"))
                {
                    if ((double) highest_y[1] <= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(vectb[1]) * (double) highest_y[1];
                    }
                }
                if (vartypes[1].equals("wr"))
                {
                    if ((double) highest_y[1] >= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(vectb[1]) * (double) highest_y[1];
                    }
                }
                //wynik = Double.parseDouble(vectb[1])*(double)highest_y[1];
            }
            if (((Double.parseDouble(vectb[1]) * (double) highest_y[1]) >= wynik) && ("min".equals(to)))
            {
                if (vartypes[1].equals("mr"))
                {
                    if ((double) highest_y[1] <= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(vectb[1]) * (double) highest_y[1];
                    }
                }
                if (vartypes[1].equals("wr"))
                {
                    if ((double) highest_y[1] >= Double.parseDouble(vars[1]))
                    {
                        wynik = Double.parseDouble(vectb[1]) * (double) highest_y[1];
                    }
                }
                //wynik = Double.parseDouble(vectb[1])*(double)highest_y[1];
            }
            System.out.println("\nWynik: " + wynik + " dla punktów (Program Dualny): y1 = " + return_points[0] + " i y2 = " + return_points[1] + ".");
            ret+="\nWynik: " + wynik + " dla punktów (Program Dualny): y1 = " + return_points[0] + " i y2 = " + return_points[1] + ".";
            double x_p,y_p;
            x_p = (Double.parseDouble(vectb[0])*Double.parseDouble(tab[1][lines[1]]) - Double.parseDouble(vectb[1])*Double.parseDouble(tab[0][lines[1]]))/(Double.parseDouble(tab[0][lines[0]])*Double.parseDouble(tab[1][lines[1]])-(Double.parseDouble(tab[0][lines[1]])*Double.parseDouble(tab[1][lines[0]])));
            y_p = (Double.parseDouble(vectb[1])*Double.parseDouble(tab[0][lines[0]]) - Double.parseDouble(vectb[0])*Double.parseDouble(tab[0][lines[1]]))/(Double.parseDouble(tab[0][lines[0]])*Double.parseDouble(tab[1][lines[1]])-(Double.parseDouble(tab[0][lines[1]])*Double.parseDouble(tab[1][lines[0]])));
            System.out.println("\nPo Powrocie do programu pierwotnego równanie wygląda tak: \n");   
            ret+="\nPo Powrocie do programu pierwotnego równanie wygląda tak: \n\n";
            for (int k = 0; k < count; k++)
            {
                for (int l = 0; l < size; l++)
                {
                    if ((l == lines[0]) || (l == lines[1]))
                    {
                        if (l != lines[1])
                        {
                            System.out.print(tab[k][l] + "x" + (l + 1) + " + ");
                            ret+=tab[k][l] + "x" + (l + 1) + " + ";
                        }
                        else
                        {
                            System.out.print(tab[k][l] + "x" + (l + 1) + " ");
                            ret+=tab[k][l] + "x" + (l + 1) + " ";
                            if ("mr".equals(signs[k]))
                            {
                                System.out.print("<=");
                                ret+="<=";
                            }
                            else if ("wr".equals(signs[k]))
                            {
                                System.out.print(">=");
                                ret+=">=";
                            }
                            else if ("r".equals(signs[k]))
                            {
                                System.out.print("=");
                                ret+="=";
                            }
                            else
                            {
                                System.out.print("unknown sign");
                            }
                        }
                    }
                }
                System.out.println(" " + vectb[k]);
                ret+=" " + vectb[k] + "\n";
            }            
            System.out.println("\nWynik: " + wynik + " dla punktów (Program Pierwotny): x1 = " + x_p + " i x2 = " + y_p + ".");
            ret+="\nWynik: " + wynik + " dla punktów (Program Pierwotny): x1 = " + x_p + " i x2 = " + y_p + ".";
            return ret;
        }
        showPP();
        System.out.println();
        showPD();
        System.out.println("Nie udało się rozwiązać. Układ nie ma wymiarów 2 x n lub n x 2.");
        ret+="Nie udało się rozwiązać. Układ nie ma wymiarów 2 x n lub n x 2.";
        return ret;
    }    
}
class Panel extends JPanel implements ActionListener
{
    JTextArea textfield = new JTextArea(30,100);
    public static final int HEIGHT = 100;
    public static final int WIDTH = 300;
    JButton load;
    JButton solve;
    XML x = new XML();
    public Panel()
    {
        load = new JButton("Wczytaj plik XML");
        solve = new JButton("Rozwiąż problem");
        load.addActionListener(this);
        solve.addActionListener(this);
        textfield.setEditable(false);
        add(load);
        add(solve);
        add(textfield);
    }

     @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == load)
        {
            x.WczytajXML("dokument2.xml");
            textfield.setText("Plik XML Wczytany"); 
        }
        if (source == solve)
        {
            textfield.setText(x.Rozwiaz());            
        }
    }
} 
class Myframe extends JFrame
{
    JButton load;
    JButton solve;
    
    public Myframe()
    {
        super("Hello World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300,100);
        setLocation(50,50);
        JPanel buttonPanel = new Panel();
		add(buttonPanel);
        pack();
        setVisible(true);
        
    }      
}
public class PD
{

    public static void main(String[] args)
    {
        XML x = new XML();
        x.WczytajXML("dokument.xml");
        //x.Rozwiaz();
        //Myframe m = new Myframe();
        EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Myframe();
			}
		});
    }
}
