package com.fp.xmol.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.fp.xmol.dao.TexamanswerDAO;
import com.fp.xmol.dao.TexamquestDAO;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.domain.Texamanswer;
import com.fp.xmol.domain.Texamquest;
import com.fp.xmol.util.Util;

public class ExamDetailVM {
	
	private TexamquestDAO texamquestDao = new TexamquestDAO();
	private TexamanswerDAO texamanswerDao = new TexamanswerDAO();
	
	private Texam obj;
	private Boolean isAdmin;
	
	private List<Texamquest> texamquestList = new ArrayList<>();
	private List<Texamanswer> texamanswerList = new ArrayList<>();
	private Map<Integer, List<Texamanswer>> mapAnswer = new HashMap<>();
	
	@Wire
	private Div divQuestList;
	@Wire
	private Div tabPage;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Texam obj, @ExecutionArgParam("isAdmin") Boolean isAdmin) {
    	Selectors.wireComponents(view, this, false);
    	this.obj = obj;
    	this.isAdmin = isAdmin;
    	doTab("evaluasi");
    	doLoad();
    	doRender();
    }
    
    public void doLoad() {	
    	try {
    		texamquestList = texamquestDao.listByFilter("texam.texampk = " + obj.getTexampk(), "texamquestpk");
    		texamanswerList = texamanswerDao.listByFilter("texamquest.texam.texampk = " + obj.getTexampk(), "texamquest.texamquestpk, answerno");
    		List<Texamanswer> oList = new ArrayList<>();
    		Integer pk = -1;
    		for (Texamanswer answer: texamanswerList) {
    			if (!answer.getTexamquest().getTexamquestpk().equals(pk)) {
    				if (oList.size() > 0)
    					mapAnswer.put(pk, oList);
    				oList = new ArrayList<>();
    				pk = answer.getTexamquest().getTexamquestpk();
    			}
    			oList.add(answer);
    		}
    		if (oList.size() > 0)
				mapAnswer.put(pk, oList);    		    	
    	} catch (Exception e) {
    		e.printStackTrace();
		}		
	}
    
    public void doRender() {	
    	try {
    		divQuestList.getChildren().clear();
    		int no = 1;
    		for (Texamquest quest: texamquestList) {    			
    			if (quest.getQuestionimg() != null) {					
					AImage aimage = new AImage("", quest.getQuestionimg());
					Image img = new Image();
					img.setContent(aimage);
					Hlayout hlayout = new Hlayout();
					hlayout.appendChild(new Label(no++ + ". "));
					Vlayout vlayout = new Vlayout();
					vlayout.appendChild(img);
					
					Textbox tbQuest = new Textbox(quest.getQuestiontext());
					tbQuest.setMultiline(true);
					tbQuest.setCols(70);
					tbQuest.setReadonly(true);
					int rows = quest.getQuestiontext().length() / 75;
					long enter = Util.countOccurrencesOf(quest.getQuestiontext(), '\n');
    				enter = enter + 1;		
    				rows += enter;					
					tbQuest.setRows(rows);
					
					vlayout.appendChild(tbQuest);
					hlayout.appendChild(vlayout);
					divQuestList.appendChild(hlayout);
				} else {					
					Hlayout hlayout = new Hlayout();
					hlayout.appendChild(new Label(no++ + ". "));					
					Textbox tbQuest = new Textbox(quest.getQuestiontext());
					tbQuest.setMultiline(true);
					tbQuest.setCols(70);
					tbQuest.setReadonly(true);
					int rows = quest.getQuestiontext().length() / 75;
					long enter = Util.countOccurrencesOf(quest.getQuestiontext(), '\n');
    				enter = enter + 1;		
    				rows += enter;					
					tbQuest.setRows(rows);
					hlayout.appendChild(tbQuest);
					divQuestList.appendChild(hlayout);	
				}    			
    			
    			Div divanswer = new Div();
    			Radiogroup radioAnswer = new Radiogroup();
    			radioAnswer.setOrient("vertical");
    			divanswer.appendChild(radioAnswer);
    			List<Texamanswer> oList = mapAnswer.get(quest.getTexamquestpk());
    			if (oList != null) {
    				for (Texamanswer answer: oList) {
    					Radio radio = new Radio(answer.getAnswerno() + ". " + answer.getAnswertext());
    					if (answer.getAnswerno().equals(quest.getRightanswer()))
    						radio.setStyle("font-weight: bold");
    					if (answer.getAnswerno().equals(quest.getHisanswer()))    						
    						radio.setSelected(true);
    					radioAnswer.appendChild(radio);    					
        			}
    			}    			
    			divQuestList.appendChild(divanswer);    			
    			
    			Label lblHasil = new Label("Nilai Jawaban : " + (quest.getIsright().equals("Y") ? "Benar" : "Salah"));
    			lblHasil.setStyle("font-weight: bold");
    			divQuestList.appendChild(lblHasil);
    			divQuestList.appendChild(new HtmlNativeComponent("hr"));
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
    
    @Command
    public void doTab(@BindingParam("tab") String tab) {
    	String path = "";
    	tabPage.getChildren().clear();
		Map<String, Object> map = new HashMap<>();
		map.put("obj", obj);
		map.put("isAdmin", isAdmin);
    	if (tab.equals("evaluasi")) {
    		path = "/view/exam/eval.zul";
    	} else if (tab.equals("memo")) {
    		path = "/view/exam/memo.zul";
    	}
    	Executions.createComponents(path, tabPage, map);
    }

	public Texam getObj() {
		return obj;
	}

	public void setObj(Texam obj) {
		this.obj = obj;
	}
    

}
