import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    private static Bolnica bolnica = new Bolnica();
    private static boolean ucitano = false;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox(10);

        root.setPadding(new Insets(10, 10, 10, 10));

        VBox vbLevi = new VBox(10);
        VBox vbDesni = new VBox(10);

        root.getChildren().addAll(vbLevi, vbDesni);

        Button btUcitaj = new Button("Ucitaj pacijente");
        Button btSledeci = new Button("Sledeci");
        Label lbBrojDana = new Label("Broj dana");
        TextField tfBrojDana = new TextField();
        Button btUbrzajVreme = new Button("Ubrazaj vreme!");
        Label lbGreska = new Label("Greska!");
        lbGreska.setVisible(false);
        lbGreska.setTextFill(Color.RED);
        RadioButton rbSve = new RadioButton("Sve");
        RadioButton rbKorona = new RadioButton("Korona");
        ToggleGroup tg = new ToggleGroup();
        rbSve.setToggleGroup(tg);
        rbKorona.setToggleGroup(tg);
        rbSve.setSelected(true);
        Button btStatistika = new Button("Statistika");
        Button btUnesi = new Button("Unesi");

        vbLevi.getChildren().addAll(btUcitaj, btSledeci, lbBrojDana, tfBrojDana,
                btUbrzajVreme, lbGreska, rbSve, rbKorona, btStatistika, btUnesi);

        Label lbCekaju = new Label("Cekaju :");
        TextArea taCekaju = new TextArea();
        taCekaju.setEditable(false);
        Label lbIzolacija = new Label("Izolacija :");
        TextArea taIzolacija = new TextArea();
        taIzolacija.setEditable(false);
        Label lbZdravi = new Label("Zdravi :");
        TextArea taZdravi = new TextArea();
        taZdravi.setEditable(false);

        btUnesi.setOnAction(e -> {
            bolnica.unesi();
        });

        btUcitaj.setOnAction(e -> {
            if(ucitano)
                return;
            bolnica.ucitaj();
            ucitano = true;
            Collections.sort(bolnica.getCekaonica());
            ispisUTextArea(bolnica.getCekaonica(), taCekaju);
        });

        btSledeci.setOnAction(e -> {
            if(bolnica.getCekaonica().isEmpty()) {
                taCekaju.clear();
                taCekaju.appendText("Niko ne ceka!");
                return;
            }

            bolnica.sledeci();
            ispisUTextArea(bolnica.getCekaonica(), taCekaju);
            ispisUTextArea(bolnica.getIzolacija(), taIzolacija);
            ispisUTextArea(bolnica.getZdravi(), taZdravi);
        });

        btUbrzajVreme.setOnAction(e -> {
            try {
                int brDana = Integer.parseInt(tfBrojDana.getText());
                if (brDana < 0) {
                    lbGreska.setVisible(true);
                    tfBrojDana.clear();
                    return;
                }

                lbGreska.setVisible(false);
                for (Pacijent pacijent : bolnica.getIzolacija()) {
                    pacijent.leci(brDana);
                }

                bolnica.getZdravi().addAll(bolnica.getIzolacija().stream()
                        .filter(Pacijent::izlecen).collect(Collectors.toList()));
                bolnica.setIzolacija(bolnica.getIzolacija().stream()
                        .filter(pacijent -> !pacijent.izlecen()).collect(Collectors.toList()));


                ispisUTextArea(bolnica.getIzolacija(), taIzolacija);
                ispisUTextArea(bolnica.getZdravi(), taZdravi);
            } catch (NumberFormatException exception) {
                lbGreska.setVisible(true);
            }
        });

        btStatistika.setOnAction(e -> {
            long brojPacijenata, brojZarazenih, brojIzlecenih;
            if(rbSve.isSelected()) {
                brojIzlecenih = bolnica.getZdravi().stream().filter(Pacijent::isZarazen).count();
                brojZarazenih = bolnica.getIzolacija().size();
                brojPacijenata = brojZarazenih + bolnica.getZdravi().size();

                taIzolacija.appendText("Procenat zarazenih : " + (100.0 * brojZarazenih / brojPacijenata) + "\n");
                taZdravi.appendText("Procenat izlecenih : " + (100.0 * brojIzlecenih / (brojZarazenih + brojIzlecenih)) + "\n");
            } else {
                brojIzlecenih = bolnica.getZdravi().stream().filter(p -> p.isZarazen() && p.getDijagnoza() instanceof Korona).count();
                brojZarazenih = bolnica.getIzolacija().stream().filter(p -> p.getDijagnoza() instanceof Korona).count();
                brojPacijenata = brojZarazenih + bolnica.getZdravi().size();

                taIzolacija.appendText("Procenat zarazenih : " + (100.0 * brojZarazenih / brojPacijenata) + "\n");
                taZdravi.appendText("Procenat izlecenih : " + (100.0 * brojIzlecenih / (brojZarazenih + brojIzlecenih)) + "\n");
            }
        });

        vbDesni.getChildren().addAll(lbCekaju, taCekaju, lbIzolacija, taIzolacija, lbZdravi, taZdravi);

        Scene scena = new Scene(root, 600, 500);
        stage.setScene(scena);
        stage.setTitle("Bolnica");
        stage.show();
    }

    private static <T> void ispisUTextArea(List<T> lista, TextArea ta) {
        ta.clear();
        for(T t : lista) {
            ta.appendText(t + "\n");
        }
    }
}