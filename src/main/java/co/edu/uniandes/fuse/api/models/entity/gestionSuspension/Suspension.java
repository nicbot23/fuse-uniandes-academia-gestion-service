package co.edu.uniandes.fuse.api.models.entity.gestionSuspension;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import co.edu.uniandes.model.util.CanonicalDateDeserializer;
import co.edu.uniandes.model.util.CanonicalDateSerializer;

public class Suspension {
	
	@JsonProperty(value = "STipoSuspension")
	private String sTipoSuspension;
	@JsonProperty(value = "SPeriodo")
	private String sPeriodo;
	@JsonProperty(value = "SCodigoSuspencion")
	private String sCodigoSuspencion;
	/*
	 * @JsonSerialize(using = CanonicalDateSerializer.class)
	 * 
	 * @JsonDeserialize(using = CanonicalDateDeserializer.class)
	 * 
	 * @JsonProperty(value = "DSuspensionDiscInicio") private Date
	 * dSuspensionDiscInicio;
	 * 
	 * @JsonSerialize(using = CanonicalDateSerializer.class)
	 * 
	 * @JsonDeserialize(using = CanonicalDateDeserializer.class)
	 * 
	 * @JsonProperty(value = "DSuspenionDiscFin") private Date dSuspenionDiscFin;
	 */
	@JsonProperty(value = "DescipcionSuspDisc")
	private String sDescipcionSuspDisc;
	@JsonProperty(value = "SsRazonSuspDisc")
	private String sRazonSuspDisc;
	
	

	public String getsTipoSuspension() {
		return sTipoSuspension;
	}

	public void setsTipoSuspension(String sTipoSuspension) {
		this.sTipoSuspension = sTipoSuspension;
	}

	public String getsPeriodo() {
		return sPeriodo;
	}

	public void setsPeriodo(String sPeriodo) {
		this.sPeriodo = sPeriodo;
	}

	public String getsCodigoSuspencion() {
		return sCodigoSuspencion;
	}

	public void setsCodigoSuspencion(String sCodigoSuspencion) {
		this.sCodigoSuspencion = sCodigoSuspencion;
	}

	/*
	 * public Date getdSuspensionDiscInicio() { return dSuspensionDiscInicio; }
	 * public void setdSuspensionDiscInicio(Date dSuspensionDiscInicio) {
	 * this.dSuspensionDiscInicio = dSuspensionDiscInicio; } public Date
	 * getdSuspenionDiscFin() { return dSuspenionDiscFin; } public void
	 * setdSuspenionDiscFin(Date dSuspenionDiscFin) { this.dSuspenionDiscFin =
	 * dSuspenionDiscFin; }
	 */
	public String getsDescipcionSuspDisc() {
		return sDescipcionSuspDisc;
	}

	public void setsDescipcionSuspDisc(String sDescipcionSuspDisc) {
		this.sDescipcionSuspDisc = sDescipcionSuspDisc;
	}

	public String getsRazonSuspDisc() {
		return sRazonSuspDisc;
	}

	public void setsRazonSuspDisc(String sRazonSuspDisc) {
		this.sRazonSuspDisc = sRazonSuspDisc;
	}

}
