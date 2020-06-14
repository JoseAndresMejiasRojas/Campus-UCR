package cr.ac.ucr.ecci.cql.campus20.foro_general.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Respuesta", foreignKeys = @ForeignKey(entity = Pregunta.class,
        parentColumns = "id",
        childColumns = "preguntaID",
        onDelete = ForeignKey.RESTRICT))
public class Respuesta {
    @PrimaryKey(autoGenerate = true)//Auto incremental
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "texto")
    public String texto;

    @ColumnInfo(name = "preguntaID")
    public int preguntaID;

    @ColumnInfo(name = "cantidad_likes")
    public int contadorLikes = 0;

    @ColumnInfo(name = "cantidad_dislikes")
    public int contadorDislikes = 0;

    public Respuesta(int id, String texto, int preguntaID, int contadorLikes, int contadorDislikes) {
        this.id = id;
        this.texto = texto;
        this.preguntaID = preguntaID;
        this.contadorLikes = contadorLikes;
        this.contadorDislikes = contadorDislikes;
    }
}