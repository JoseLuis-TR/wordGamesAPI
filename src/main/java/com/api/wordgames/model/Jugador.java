package com.api.wordgames.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "jugador")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "admin", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean admin = false;

    @Column(name = "nombreusu", length = 50, nullable = false)
    private String nombreusu;

    @Column(name = "clave", length = 255, nullable = false)
    private String clave;

    @Column(name = "avatar", length = 255)
    private String avatar = "";

    @Column(name = "puntos", nullable = false)
    private Integer puntos = 0;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="id_equipo")
    private Equipo equipo = null;

    @JsonManagedReference
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Partida> partidas;
}
