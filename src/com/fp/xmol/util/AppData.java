package com.fp.xmol.util;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Comboitem;

public class AppData {

	public static String getStatusLabel(String status) {
		String desc = "";
		if (status.equals(AppUtil.STATUS_TES))
			desc = "Tes";
		else if (status.equals(AppUtil.STATUS_GAGALTES))
			desc = "Gagal Tes";
		else if (status.equals(AppUtil.STATUS_MENUNGGUINTERVIEW))
			desc = "Menunggu Interview";
		else if (status.equals(AppUtil.STATUS_BATALINTERVIEW))
			desc = "Batal Interview";		
		else if (status.equals(AppUtil.STATUS_EVALUASIINTERVIEW))
			desc = "Evaluasi Interview";
		else if (status.equals(AppUtil.STATUS_GAGALINTERVIEW))
			desc = "Tidak Lulus Interview";
		else if (status.equals(AppUtil.STATUS_MENUNGGUPSIKOTES))
			desc = "Menunggu Psikotes";
		else if (status.equals(AppUtil.STATUS_BATALPSIKOTES))
			desc = "Batal Psikotes";
		else if (status.equals(AppUtil.STATUS_EVALUASIPSIKOTES))
			desc = "Evaluasi Psikotes";
		else if (status.equals(AppUtil.STATUS_GAGALPSIKOTES))
			desc = "Tidak Lulus Psikotes";
		else if (status.equals(AppUtil.STATUS_MENUNGGUREKRUT))
			desc = "Menunggu Rekrutmen";
		else if (status.equals(AppUtil.STATUS_GAGALREKRUT))
			desc = "Gagal Rekrutmen";
		else if (status.equals(AppUtil.STATUS_SUKSESREKRUT))
			desc = "Sukses Rekrutmen";
		return desc;
	}
	
	public static List<Comboitem> getStatusCombo() {
		List<Comboitem> listItem = new ArrayList<Comboitem>();
		Comboitem item = new Comboitem();
		item.setLabel("All");
		item.setValue("All");
		listItem.add(item);
		Comboitem item1 = new Comboitem();
		item1.setLabel(getStatusLabel(AppUtil.STATUS_TES));
		item1.setValue(AppUtil.STATUS_TES);
		listItem.add(item1);
		Comboitem item2 = new Comboitem();
		item2.setLabel(getStatusLabel(AppUtil.STATUS_GAGALTES));
		item2.setValue(AppUtil.STATUS_GAGALTES);
		listItem.add(item2);
		Comboitem item3 = new Comboitem();
		item3.setLabel(getStatusLabel(AppUtil.STATUS_MENUNGGUINTERVIEW));
		item3.setValue(AppUtil.STATUS_MENUNGGUINTERVIEW);
		listItem.add(item3);
		Comboitem item4 = new Comboitem();
		item4.setLabel(getStatusLabel(AppUtil.STATUS_BATALINTERVIEW));
		item4.setValue(AppUtil.STATUS_BATALINTERVIEW);
		listItem.add(item4);
		Comboitem item5 = new Comboitem();
		item5.setLabel(getStatusLabel(AppUtil.STATUS_EVALUASIINTERVIEW));
		item5.setValue(AppUtil.STATUS_EVALUASIINTERVIEW);
		listItem.add(item5);
		Comboitem item6 = new Comboitem();
		item6.setLabel(getStatusLabel(AppUtil.STATUS_GAGALINTERVIEW));
		item6.setValue(AppUtil.STATUS_GAGALINTERVIEW);
		listItem.add(item6);
		Comboitem item7 = new Comboitem();
		item7.setLabel(getStatusLabel(AppUtil.STATUS_MENUNGGUPSIKOTES));
		item7.setValue(AppUtil.STATUS_MENUNGGUPSIKOTES);
		listItem.add(item7);
		Comboitem item8 = new Comboitem();
		item8.setLabel(getStatusLabel(AppUtil.STATUS_BATALPSIKOTES));
		item8.setValue(AppUtil.STATUS_BATALPSIKOTES);
		listItem.add(item8);
		Comboitem item9 = new Comboitem();
		item9.setLabel(getStatusLabel(AppUtil.STATUS_EVALUASIPSIKOTES));
		item9.setValue(AppUtil.STATUS_EVALUASIPSIKOTES);
		listItem.add(item9);
		Comboitem item10 = new Comboitem();
		item10.setLabel(getStatusLabel(AppUtil.STATUS_GAGALPSIKOTES));
		item10.setValue(AppUtil.STATUS_GAGALPSIKOTES);
		listItem.add(item10);
		Comboitem item11 = new Comboitem();
		item11.setLabel(getStatusLabel(AppUtil.STATUS_MENUNGGUREKRUT));
		item11.setValue(AppUtil.STATUS_MENUNGGUREKRUT);
		listItem.add(item11);
		Comboitem item12 = new Comboitem();
		item12.setLabel(getStatusLabel(AppUtil.STATUS_GAGALREKRUT));
		item12.setValue(AppUtil.STATUS_GAGALREKRUT);
		listItem.add(item12);
		Comboitem item13 = new Comboitem();
		item13.setLabel(getStatusLabel(AppUtil.STATUS_SUKSESREKRUT));
		item13.setValue(AppUtil.STATUS_SUKSESREKRUT);
		listItem.add(item13);
		return listItem;
	}
	
	public static List<Comboitem> getStatusComboReq(String status) {
		List<Comboitem> listItem = new ArrayList<Comboitem>();
		if (status.equals(AppUtil.STATUS_TES)) {
			Comboitem item1 = new Comboitem();
			item1.setLabel("Request Interview");
			item1.setValue(AppUtil.STATUS_MENUNGGUINTERVIEW);
			listItem.add(item1);
			Comboitem item2 = new Comboitem();
			item2.setLabel(getStatusLabel(AppUtil.STATUS_GAGALTES));
			item2.setValue(AppUtil.STATUS_GAGALTES);
			listItem.add(item2);
		} else if (status.equals(AppUtil.STATUS_EVALUASIINTERVIEW)) {
			Comboitem item1 = new Comboitem();
			item1.setLabel("Request Psikotes");
			item1.setValue(AppUtil.STATUS_MENUNGGUPSIKOTES);
			listItem.add(item1);
			Comboitem item2 = new Comboitem();
			item2.setLabel(getStatusLabel(AppUtil.STATUS_GAGALINTERVIEW));
			item2.setValue(AppUtil.STATUS_GAGALINTERVIEW);
			listItem.add(item2);
		} else if (status.equals(AppUtil.STATUS_EVALUASIPSIKOTES)) {
			Comboitem item1 = new Comboitem();
			item1.setLabel("Request Rekrutmen");
			item1.setValue(AppUtil.STATUS_MENUNGGUREKRUT);
			listItem.add(item1);
			Comboitem item2 = new Comboitem();
			item2.setLabel(getStatusLabel(AppUtil.STATUS_GAGALPSIKOTES));
			item2.setValue(AppUtil.STATUS_GAGALPSIKOTES);
			listItem.add(item2);
		} 
		return listItem;
	}
	
	public static List<Comboitem> getStatusComboResp(String status) {
		List<Comboitem> listItem = new ArrayList<Comboitem>();
		if (status.equals(AppUtil.STATUS_MENUNGGUINTERVIEW)) {
			Comboitem item1 = new Comboitem();
			item1.setLabel("Telah Interview");
			item1.setValue(AppUtil.STATUS_EVALUASIINTERVIEW);
			listItem.add(item1);
			Comboitem item2 = new Comboitem();
			item2.setLabel(getStatusLabel(AppUtil.STATUS_BATALINTERVIEW));
			item2.setValue(AppUtil.STATUS_BATALINTERVIEW);
			listItem.add(item2);
		} else if (status.equals(AppUtil.STATUS_MENUNGGUPSIKOTES)) {
			Comboitem item1 = new Comboitem();
			item1.setLabel("Telah Psikotes");
			item1.setValue(AppUtil.STATUS_EVALUASIPSIKOTES);
			listItem.add(item1);
			Comboitem item2 = new Comboitem();
			item2.setLabel(getStatusLabel(AppUtil.STATUS_BATALPSIKOTES));
			item2.setValue(AppUtil.STATUS_BATALPSIKOTES);
			listItem.add(item2);
		} else if (status.equals(AppUtil.STATUS_MENUNGGUREKRUT)) {
			Comboitem item1 = new Comboitem();
			item1.setLabel(getStatusLabel(AppUtil.STATUS_SUKSESREKRUT));
			item1.setValue(AppUtil.STATUS_SUKSESREKRUT);
			listItem.add(item1);
			Comboitem item2 = new Comboitem();
			item2.setLabel(getStatusLabel(AppUtil.STATUS_GAGALREKRUT));
			item2.setValue(AppUtil.STATUS_GAGALREKRUT);
			listItem.add(item2);
		} 
		return listItem;
	}
}
