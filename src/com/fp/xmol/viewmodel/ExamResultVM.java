package com.fp.xmol.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.fp.utils.SysUtils;
import com.fp.xmol.dao.MdepartmentDAO;
import com.fp.xmol.dao.TexamDAO;
import com.fp.xmol.domain.Mdepartment;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.model.TexamListModel;
import com.fp.xmol.util.AppData;

public class ExamResultVM {
	
	private TexamDAO oDao = new TexamDAO();
	private TexamListModel model;
	
	private Texam objForm;
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String filter;
	private Mdepartment dept;
	private String kodesoal;
	private String ispass;
	private String nama;
	private String status;
	private String statusreq;
	private Date startdate;
	private Date enddate;
	
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat datetimeLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	@Wire
	private Paging paging;
	@Wire
	private Listbox listbox;
	@Wire
	private Combobox cbDept;
	@Wire
	private Combobox cbIspass;
	@Wire
	private Combobox cbStatus;
	@Wire
	private Row rowStatus;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("statusreq") String statusreq){
    	Selectors.wireComponents(view, this, false);
    	this.statusreq = statusreq;
    	if (statusreq != null) {
    		status = statusreq;
    		rowStatus.setVisible(false);
    	} else {
    		for (Comboitem item: AppData.getStatusCombo()) {
    			cbStatus.appendChild(item);
    		}
    	}
    	    	
    	paging.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumber = pe.getActivePage();
				refreshModel(pageStartNumber);

			}
		});
    	
    	listbox.setItemRenderer(new ListitemRenderer<Texam>() {

			@Override
			public void render(Listitem item, final Texam data, int index)
					throws Exception {
				Listcell cell = new Listcell(String.valueOf(index + 1));
				item.appendChild(cell);
				cell = new Listcell(data.getDeptcode());
				item.appendChild(cell);				
				cell = new Listcell(data.getNama());
				item.appendChild(cell);
				cell = new Listcell(dateLocalFormatter.format(data.getTgllahir()));
				item.appendChild(cell);
				cell = new Listcell(data.getTbanksoal().getKodesoal());
				item.appendChild(cell);
				cell = new Listcell(dateLocalFormatter.format(data.getWaktumulaitest()));
				item.appendChild(cell);
				cell = new Listcell(String.valueOf(data.getPassingscore()));
				item.appendChild(cell);	
				cell = new Listcell(String.valueOf(data.getScore()));
				item.appendChild(cell);	
				cell = new Listcell(data.getIspass().equals("Y") ? "Lulus" : data.getIspass().equals("N") ? "Tidak Lulus" : "On Progress");
				item.appendChild(cell);
				cell = new Listcell(AppData.getStatusLabel(data.getStatus()));
				item.appendChild(cell);
				
				Button btLink = new Button("Dokumen (" + data.getTotaldoc() + ")");
				btLink.setAutodisable("self");
				btLink.setSclass("btn btn-success btn-sm");
				btLink.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						Window win = (Window) Executions
								.createComponents("/view/exam/examdoc.zul", null, map);
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								refreshModel(pageStartNumber);
								BindUtils.postNotifyChange(null, null, ExamResultVM.this, "*");
							}
						});
					}
				});	
				cell = new Listcell();
				cell.appendChild(btLink);
				item.appendChild(cell);
			}
		});
    	
    	listbox.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (listbox.getSelectedIndex() != -1) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("obj", objForm);
					if (statusreq != null) 
						map.put("isAdmin", new Boolean(true));
					Window win = (Window) Executions
							.createComponents("/view/exam/examdetail.zul", null, map);
					win.setClosable(true);
					win.doModal();
					win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							needsPageUpdate = true;
							refreshModel(pageStartNumber);
							BindUtils.postNotifyChange(null, null, ExamResultVM.this, "*");
						}
					});
				}
			}
		});
    	
    	doReset();    	
    }
    
    @NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {	
    	paging.setPageSize(SysUtils.PAGESIZE);
		model = new TexamListModel(activePage, SysUtils.PAGESIZE, filter,
				"texampk desc");
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		paging.setTotalSize(pageTotalSize);
		listbox.setModel(model);		
	}
    
    @Command
	@NotifyChange("pageTotalSize")
	public void doSearch() {
    	filter = "";
    	if (dept != null) {
    		if (filter.length() > 0)
    			filter += " and ";
    		filter += "deptcode = '" + dept.getDeptcode() + "'";
    	}
    	if (kodesoal != null) {
    		if (filter.length() > 0)
    			filter += " and ";
    		filter += "tbanksoal.kodesoal = '" + kodesoal + "'";
    	}
    	if (!ispass.equals("All")) {
    		if (filter.length() > 0)
    			filter += " and ";
    		filter += "ispass = '" + ispass + "'";
    	}
    	if (nama != null) {
    		if (filter.length() > 0)
    			filter += " and ";
    		filter += "upper(nama) like '%" + nama.trim().toUpperCase() + "%'";
    	}
    	if (!status.equals("All")) {
    		if (filter.length() > 0)
    			filter += " and ";
    		filter += "status = '" + status + "'";
    	}
    	if (startdate != null && enddate != null) {
    		if (filter.length() > 0)
    			filter += " and ";
    		filter += "date(waktumulaitest) between '" + new SimpleDateFormat("yyyy-MM-dd").format(startdate) + "' and '" + new SimpleDateFormat("yyyy-MM-dd").format(enddate) + "'";
    	}
    	needsPageUpdate = true;
		paging.setActivePage(0);
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
    }
    
    @Command
    @NotifyChange("*")    
	public void doReset() {		
		objForm = null;
		dept = null;
		kodesoal = null;
		nama = null;
		ispass = "All";
		if (statusreq == null)
			status = "All";
		startdate = null;
		enddate = null;
		cbDept.setValue(null);
		cbIspass.setValue("All");
		cbStatus.setValue("All");
		doSearch();
	}
    
    @Command
	public void doExport() {
		try {
			List<Texam> listData = oDao.listByFilter(filter, "texampk desc");
			if (listData != null && listData.size() > 0) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet();

				int rownum = 0;
				int cellnum = 0;
				Integer no = 0;
				org.apache.poi.ss.usermodel.Row row = sheet.createRow(rownum++);
				Cell cell = row.createCell(0);
				cell.setCellValue("Data Evaluasi Kandidat");
				rownum++;
				
				Map<Integer, Object[]> datamap = new TreeMap<Integer, Object[]>();
				datamap.put(1, new Object[] { "No", "Departemen", "Nama", "Tgl Lahir", "Alamat", "Kota", "No HP", "E-mail", "Waktu Mulai Tes", "Waktu Selesai Tes", "Lama Tes (Menit)",
						"Kode Soal", "Jumlah Soal", "Passing Score", "Jumlah Benar", "Score", "Hasil Tes", "Status" });
				no = 2;
				for (Texam data: listData) {								
					datamap.put(
							no,
							new Object[] {
									no - 1, data.getDeptcode(), data.getNama(), dateLocalFormatter.format(data.getTgllahir()), data.getAlamat(), data.getKota(), 
									data.getNohp(), data.getEmail(), datetimeLocalFormatter.format(data.getWaktumulaitest()), data.getWaktuakhirtest() != null ? datetimeLocalFormatter.format(data.getWaktuakhirtest()) : "", 
									data.getLamates(), data.getTbanksoal().getKodesoal(), data.getJumlahsoal(), 
									data.getPassingscore(), data.getJumlahbenar(), data.getScore(), data.getIspass().equals("Y") ? "Lulus" : data.getIspass().equals("N") ? "Tidak Lulus" :"On Progress", 
									AppData.getStatusLabel(data.getStatus())});
					no++;
				}					
				Set<Integer> keyset = datamap.keySet();
				for (Integer key : keyset) {
					row = sheet.createRow(rownum++);
					Object[] objArr = datamap.get(key);
					cellnum = 0;
					for (Object obj : objArr) {
						cell = row.createCell(cellnum++);
						if (obj instanceof String)
							cell.setCellValue((String) obj);
						else if (obj instanceof Integer)
							cell.setCellValue((Integer) obj);
						else if (obj instanceof Double)
							cell.setCellValue((Double) obj);
					}
				}									

				String path = Executions
						.getCurrent()
						.getDesktop()
						.getWebApp()
						.getRealPath(
								SysUtils.FILES_ROOT_PATH + SysUtils.REPORT_PATH);
				String filename = "XMOL_EVALUASI_"
						+ new SimpleDateFormat("yyMMddHHmm").format(new Date())
						+ ".xlsx";
				FileOutputStream out = new FileOutputStream(new File(path + "/"
						+ filename));
				workbook.write(out);
				out.close();

				Filedownload
						.save(new File(path + "/" + filename),
								"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				Messagebox.show("Data tidak tersedia", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.INFORMATION);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
					Messagebox.OK, Messagebox.ERROR);
		}
	}

	public Texam getObjForm() {
		return objForm;
	}

	public void setObjForm(Texam objForm) {
		this.objForm = objForm;
	}

	public ListModelList<Mdepartment> getMdepartment() {
		ListModelList<Mdepartment> oList = null;
		try {
			oList = new ListModelList<>(new MdepartmentDAO().listByFilter("0=0", "deptcode"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public Mdepartment getDept() {
		return dept;
	}

	public void setDept(Mdepartment dept) {
		this.dept = dept;
	}

	public String getKodesoal() {
		return kodesoal;
	}

	public void setKodesoal(String kodesoal) {
		this.kodesoal = kodesoal;
	}

	public String getIspass() {
		return ispass;
	}

	public void setIspass(String ispass) {
		this.ispass = ispass;
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
}
