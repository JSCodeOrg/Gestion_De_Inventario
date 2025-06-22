package com.JSCode.gestion_de_inventario.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne

    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagenes> imagenes = new ArrayList<>();

    @Column(name = "palabras_clave")
    private String palabrasClave;

    @Column()
    private LocalDateTime deletedAt;
    private long idProducto;

    public void setIdProducto(long idProducto) {
        // TODO Auto-generated method stub
        this.id = idProducto;
        this.idProducto = idProducto;
    } 
    public long getIdProducto() {
        // TODO Auto-generated method stub
        return this.id;
    }
}
