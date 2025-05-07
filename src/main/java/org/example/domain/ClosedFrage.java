package org.example.domain;

public class ClosedFrage extends Frage {

	private CloseType closedFragentype;

	public ClosedFrage(int id, String frageText, BloomLevel bloomLevel, Modul module, int geschaetzeZeit, FragenArt fragenArt, CloseType closedFragentype) {
		super(id, frageText, bloomLevel, module, geschaetzeZeit, fragenArt);
		this.closedFragentype = closedFragentype;
	}

	public CloseType getClosedFragentype() {
		return closedFragentype;
	}

}