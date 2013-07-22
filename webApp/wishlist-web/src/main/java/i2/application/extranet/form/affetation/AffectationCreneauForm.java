package i2.application.extranet.form.affetation;

public class AffectationCreneauForm {

    private Integer actId;

    private int index;

    private int nbUnites;

    public Integer getActId() {
	return actId;
    }

    public void setActId(Integer actId) {
	this.actId = actId;
    }

    public int getIndex() {
	return index;
    }

    public void setIndex(int index) {
	this.index = index;
    }

    public int getNbUnites() {
	return nbUnites;
    }

    public void setNbUnites(int nbUnites) {
	this.nbUnites = nbUnites;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("AffectationCreneauForm [actId=");
	builder.append(getActId());
	builder.append(", index=");
	builder.append(getIndex());
	builder.append(", nbUnites=");
	builder.append(getNbUnites());
	builder.append("]");
	return builder.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((actId == null) ? 0 : actId.hashCode());
	result = prime * result + index;
	result = prime * result + nbUnites;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof AffectationCreneauForm))
	    return false;
	AffectationCreneauForm other = (AffectationCreneauForm) obj;
	if (actId == null) {
	    if (other.actId != null)
		return false;
	} else if (!actId.equals(other.actId))
	    return false;
	if (index != other.index)
	    return false;
	if (nbUnites != other.nbUnites)
	    return false;
	return true;
    }

}
