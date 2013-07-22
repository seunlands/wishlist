package i2.application.extranet.action.affectation;

/**
 * Index d'une table pour trouver les unités affectées à partir d'une activite et de l'index du créneau horaire.
 * 
 * @author bull
 * 
 */
public class AffectationCreneauHoraireIndex {

    private int actId;

    private int creneauHoraireIndex;

    public AffectationCreneauHoraireIndex(int actId, int creneauHoraireIndex) {
	this.actId = actId;
	this.creneauHoraireIndex = creneauHoraireIndex;
    }

    public int getActId() {
	return actId;
    }

    public int getCreneauHoraireIndex() {
	return creneauHoraireIndex;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + actId;
	result = prime * result + creneauHoraireIndex;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AffectationCreneauHoraireIndex other = (AffectationCreneauHoraireIndex) obj;
	if (actId != other.actId)
	    return false;
	if (creneauHoraireIndex != other.creneauHoraireIndex)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("AffectationCreneauHoraireIndex [actId=");
	builder.append(actId);
	builder.append(", creneauHoraireIndex=");
	builder.append(creneauHoraireIndex);
	builder.append("]");
	return builder.toString();
    }

}
