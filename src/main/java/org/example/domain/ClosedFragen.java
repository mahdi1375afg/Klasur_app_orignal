package org.example.domain;

public class ClosedFragen extends Fragen {

	private CloseType closedFragentype;

	public ClosedFragen(int id,String frageText, BloomLevel bloomLevel, Modul module, int geschaetzeZeit, FragenArt fragenArt, CloseType closedFragentype) {
		super(id, frageText, bloomLevel, module, geschaetzeZeit, fragenArt);
		this.closedFragentype = closedFragentype;
	}

	public CloseType getClosedFragentype() {
		return closedFragentype;
	}

}