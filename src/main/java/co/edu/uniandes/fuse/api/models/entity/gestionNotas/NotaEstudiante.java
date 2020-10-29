package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import co.edu.uniandes.model.util.CanonicalDateDeserializer;

import co.edu.uniandes.model.util.CanonicalDateSerializer;

public class NotaEstudiante {

	
//----------------------------------------------------------------------------------------	
//---------------- ATRIBUTES -------------------------------------------------------------
//----------------------------------------------------------------------------------------
	
	@JsonProperty(value = "SPidm")
	private String sPidm;
	@JsonProperty(value = "SCRN")
	private String sCRN;
	@JsonProperty(value = "SNombreCurso")
	private String sNombreCurso;
	@JsonProperty(value = "SCodigoCurso")
	private String sCodigoCurso;
	@JsonProperty(value = "SPeriodo")
	private String sPeriodo;
	@JsonProperty(value = "SNivel")
	private String sNivel;
	//@JsonSerialize(using = CanonicalDateSerializer.class, include = Inclusion.NON_NULL) //comentado
    //@JsonDeserialize(using = CanonicalDateDeserializer.class)//comentado

	//	@JsonSerialize(using = CanonicalDateSerializer.class)
//    @JsonDeserialize(using = CanonicalDateDeserializer.class)
	@JsonProperty(value = "DFechaNota")
	private Date dFechaNota;
//	@JsonSerialize(using = CanonicalDateSerializer.class, include = Inclusion.NON_NULL)
//    @JsonDeserialize(using = CanonicalDateDeserializer.class)
	@JsonProperty(value = "SNota")
	private String sNota;
	@JsonProperty(value = "LNotasParciales")
	private List notasParciales;
	
//----------------------------------------------------------------------------------------	
//---------------- METHODS ---------------------------------------------------------------
//----------------------------------------------------------------------------------------		
	
	
	
	public String getsPidm() {
		return sPidm;
	}
	public void setsPidm(String sPidm) {
		this.sPidm = sPidm;
	}
	public String getsCRN() {
		return sCRN;
	}
	public void setsCRN(String sCRN) {
		this.sCRN = sCRN;
	}
	public String getsNombreCurso() {
		return sNombreCurso;
	}
	public void setsNombreCurso(String sNombreCurso) {
		this.sNombreCurso = sNombreCurso;
	}
	public String getsCodigoCurso() {
		return sCodigoCurso;
	}
	public void setsCodigoCurso(String sCodigoCurso) {
		this.sCodigoCurso = sCodigoCurso;
	}
	public String getsPeriodo() {
		return sPeriodo;
	}
	public void setsPeriodo(String sPeriodo) {
		this.sPeriodo = sPeriodo;
	}
	public String getsNivel() {
		return sNivel;
	}
	public void setsNivel(String sNivel) {
		this.sNivel = sNivel;
	}
	public Date getdFechaNota() {
		return dFechaNota;
	}
	public void setdFechaNota(Date dFechaNota) {
		this.dFechaNota = dFechaNota;
	}
	public String getsNota() {
		return sNota;
	}
	public void setsNota(String sNota) {
		this.sNota = sNota;
	}
	public List getNotasParciales() {
		return notasParciales;
	}
	public void setNotasParciales(List notasParciales) {
		this.notasParciales = notasParciales;
	}
}
