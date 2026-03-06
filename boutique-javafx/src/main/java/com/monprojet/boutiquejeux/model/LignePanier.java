package com.monprojet.boutiquejeux.model;

import javafx.beans.property.*;

public class LignePanier {

    private final LongProperty    produitId  = new SimpleLongProperty();
    private final StringProperty  nomProduit = new SimpleStringProperty();
    private final StringProperty  type       = new SimpleStringProperty();
    private final IntegerProperty quantite   = new SimpleIntegerProperty();
    private final DoubleProperty  prix       = new SimpleDoubleProperty();

    public LignePanier(Long produitId, String nom, String type, int quantite, double prix) {
        this.produitId.set(produitId);
        this.nomProduit.set(nom);
        this.type.set(type);
        this.quantite.set(quantite);
        this.prix.set(prix);
    }

    public Long    getProduitId()  { return produitId.get(); }
    public String  getNomProduit() { return nomProduit.get(); }
    public String  getType()       { return type.get(); }
    public int     getQuantite()   { return quantite.get(); }
    public double  getPrix()       { return prix.get(); }
    public String  getPrixAffiche(){ return String.format("%.2f €", prix.get() * quantite.get()); }

    public void setQuantite(int q) { quantite.set(q); }

    public LongProperty    produitIdProperty()  { return produitId; }
    public StringProperty  nomProduitProperty() { return nomProduit; }
    public StringProperty  typeProperty()       { return type; }
    public IntegerProperty quantiteProperty()   { return quantite; }
    public DoubleProperty  prixProperty()       { return prix; }
}
