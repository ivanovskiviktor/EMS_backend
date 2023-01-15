package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.*;
import graduatethesis.performancemonitoringsystem.model.filters.EmployeeTrackingFormFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.organization.*;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.organization.EmployeeTrackingFormRepository;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeTrackingFormServiceImpl implements EmployeeTrackingFormService {

    private final EmployeeTrackingFormRepository employeeTrackingFormRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LoggedUserService loggedUserService;
    private final WorkingItemService workingItemService;
    private final PrivilegeService privilegeService;
    private final EmployeeTrackingFormStatusService employeeTrackingFormStatusService;
    private XSSFSheet sheet;
    private XSSFWorkbook workbook;
    private final OrganizationalDepartmentService organizationalDepartmentService;
    private final OrganizationalDepartmentWorkingItemService organizationalDepartmentWorkingItemService;
    private final EmployeeTrackingFormUserService employeeTrackingFormUserService;

    public EmployeeTrackingFormServiceImpl(EmployeeTrackingFormRepository employeeTrackingFormRepository, UserRepository userRepository, UserService userService, LoggedUserService loggedUserService, WorkingItemService workingItemService, PrivilegeService privilegeService, EmployeeTrackingFormStatusService employeeTrackingFormStatusService, OrganizationalDepartmentService organizationalDepartmentService, OrganizationalDepartmentWorkingItemService organizationalDepartmentWorkingItemService, EmployeeTrackingFormUserService employeeTrackingFormUserService) {
        this.employeeTrackingFormRepository = employeeTrackingFormRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
        this.workingItemService = workingItemService;
        this.privilegeService = privilegeService;
        this.employeeTrackingFormStatusService = employeeTrackingFormStatusService;
        this.organizationalDepartmentService = organizationalDepartmentService;
        this.organizationalDepartmentWorkingItemService = organizationalDepartmentWorkingItemService;
        this.employeeTrackingFormUserService = employeeTrackingFormUserService;
    }


    @Override
    public EmployeeTrackingForm create(EmployeeTrackingForm employeeTrackingForm) {
        return this.employeeTrackingFormRepository.save(employeeTrackingForm);
    }

    @Override
    public List<EmployeeTrackingForm> findAll() {
        return this.employeeTrackingFormRepository.findAll();
    }

    @Override
    public Optional<EmployeeTrackingForm> save(EmployeeTrackingFormHelper employeeTrackingFormHelper) {
        if(employeeTrackingFormHelper.getUserIds()==null || employeeTrackingFormHelper.getUserIds().size()==0){
            throw new UsersNotSpecifiedException();
        }
        EmployeeTrackingForm employeeTrackingForm = new EmployeeTrackingForm();

        WorkingItem workingItem = this.workingItemService.findById(employeeTrackingFormHelper.getWorkingItemId())
                .orElseThrow(()->new WorkingItemNotFoundException(employeeTrackingFormHelper.getWorkingItemId()));
        Status status = this.employeeTrackingFormStatusService.findById(employeeTrackingFormHelper.getEmployeeTrackingFormStatusId())
                .orElseThrow(()->new StatusNotFound(employeeTrackingFormHelper.getEmployeeTrackingFormStatusId()));
        OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentService.findById(employeeTrackingFormHelper.getOrganizationalDepartmentId());


        employeeTrackingForm.setTaskStartDate(employeeTrackingFormHelper.getTaskStartDate());
        employeeTrackingForm.setTaskEndDate(employeeTrackingFormHelper.getTaskEndDate());
        employeeTrackingForm.setDescription(employeeTrackingFormHelper.getDescription());
        employeeTrackingForm.setValue(employeeTrackingFormHelper.getValue());
        employeeTrackingForm.setTitle(employeeTrackingFormHelper.getTitle());

        OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem = this.organizationalDepartmentWorkingItemService.findByOrganizationalDepartmentAndWorkingItem(organizationalDepartment.getId(), workingItem.getId());


        employeeTrackingForm.setOrganizationalDepartmentWorkingItem(organizationalDepartmentWorkingItem);
        employeeTrackingForm.setStatus(status);
        employeeTrackingForm.setUser(loggedUserService.getLoggedUser());

        employeeTrackingForm = Optional.of(this.employeeTrackingFormRepository.save(employeeTrackingForm)).get();

        if(employeeTrackingFormHelper.getUserIds()!=null && employeeTrackingFormHelper.getUserIds().size()>0){
            for(int i=0; i<employeeTrackingFormHelper.getUserIds().size(); i++){
                EmployeeTrackingFormUser etfu = new EmployeeTrackingFormUser();
                etfu.setUser(userService.findById(employeeTrackingFormHelper.getUserIds().get(i)));
                etfu.setEmployeeTrackingForm(employeeTrackingForm);

                employeeTrackingFormUserService.save(etfu);
            }
        }

        return Optional.of(employeeTrackingForm);
    }


    @Override
    public void deleteById(Long id) {
        EmployeeTrackingForm employeeTrackingForm = this.employeeTrackingFormRepository.findById(id).orElseThrow(()-> new EmployeeTrackingFormNotFoundException(id));
        if(employeeTrackingForm.getReports().size() == 0) {
            if (employeeTrackingForm.getTimeTrackingFormUsers() != null && employeeTrackingForm.getTimeTrackingFormUsers().size() > 0) {
                for (int i = 0; i < employeeTrackingForm.getTimeTrackingFormUsers().size(); i++) {
                    this.employeeTrackingFormUserService.deleteByEmployeeTrackingFormAndUser(employeeTrackingForm.getId(), employeeTrackingForm.getTimeTrackingFormUsers().get(i).getUser().getId());
                }
            }
        }

        this.employeeTrackingFormRepository.deleteById(employeeTrackingForm.getId());


    }

    @Override
    public Optional<EmployeeTrackingForm> update(EmployeeTrackingFormHelper employeeTrackingFormHelper) {
        EmployeeTrackingForm employeeTrackingForm = this.employeeTrackingFormRepository.findById(employeeTrackingFormHelper.getId()).orElseThrow(() -> new EmployeeTrackingFormNotFoundException(employeeTrackingFormHelper.getId()));
        WorkingItem workingItem = this.workingItemService.findById(employeeTrackingFormHelper.getWorkingItemId()).orElseThrow(()->new WorkingItemNotFoundException(employeeTrackingFormHelper.getWorkingItemId()));
        Status status = this.employeeTrackingFormStatusService.findById(employeeTrackingFormHelper.getEmployeeTrackingFormStatusId()).orElseThrow(()-> new StatusNotFound(employeeTrackingFormHelper.getEmployeeTrackingFormStatusId()));
        OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentService.findById(employeeTrackingFormHelper.getOrganizationalDepartmentId());

        employeeTrackingForm.setId(employeeTrackingFormHelper.getId());
        employeeTrackingForm.setTaskStartDate(employeeTrackingFormHelper.getTaskStartDate());
        employeeTrackingForm.setTaskEndDate(employeeTrackingFormHelper.getTaskEndDate());
        employeeTrackingForm.setDescription(employeeTrackingFormHelper.getDescription());
        employeeTrackingForm.setValue(employeeTrackingFormHelper.getValue());
        employeeTrackingForm.setStatus(status);
        employeeTrackingForm.setTitle(employeeTrackingFormHelper.getTitle());
        employeeTrackingForm.setUser(loggedUserService.getLoggedUser());
        employeeTrackingForm.setDateModified(OffsetDateTime.now());

        OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem = this.organizationalDepartmentWorkingItemService.findByOrganizationalDepartmentAndWorkingItem(organizationalDepartment.getId(), workingItem.getId());
        employeeTrackingForm.setOrganizationalDepartmentWorkingItem(organizationalDepartmentWorkingItem);

        employeeTrackingForm = this.employeeTrackingFormRepository.save(employeeTrackingForm);

        if(employeeTrackingFormHelper.getUserIds()!=null && employeeTrackingFormHelper.getUserIds().size()>0){
            for(int i=0; i<employeeTrackingForm.getTimeTrackingFormUsers().size(); i++){
                this.employeeTrackingFormUserService.deleteByEmployeeTrackingFormAndUser(employeeTrackingForm.getId(), employeeTrackingForm.getTimeTrackingFormUsers().get(i).getUser().getId());
            }
            for(int i=0; i<employeeTrackingFormHelper.getUserIds().size(); i++){
                EmployeeTrackingFormUser etfu = new EmployeeTrackingFormUser();
                etfu.setUser(userService.findById(employeeTrackingFormHelper.getUserIds().get(i)));
                etfu.setEmployeeTrackingForm(employeeTrackingForm);

                employeeTrackingFormUserService.save(etfu);
            }
        }

        return Optional.of(employeeTrackingForm);

    }

    @Override
    public List<EmployeeTrackingForm> findAllCustom(EmployeeTrackingFormFilter employeeTrackingFormFilter) {
        return this.employeeTrackingFormRepository.findAllCustom(employeeTrackingFormFilter);
    }

    @Override
    public Page<EmployeeTrackingForm> findAllCustomPaginated(String status, EmployeeTrackingFormFilter employeeTrackingFormFilter, Pageable pageable) {
        return this.employeeTrackingFormRepository.findAllCustomPaginated(status, employeeTrackingFormFilter, pageable);
    }

    @Override
    public List<EmployeeTrackingForm> findAllByUser(EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id) {
        return this.employeeTrackingFormRepository.findAllByUser(employeeTrackingFormFilter, id);
    }

    @Override
    public Page<EmployeeTrackingForm> findAllByUserPaginated(String status, EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id, Pageable pageable) {
        return this.employeeTrackingFormRepository.findAllByUserPaginated(status, employeeTrackingFormFilter, id, pageable);
    }

    @Override
    public List<EmployeeTrackingForm> findAllFormsForUser(EmployeeTrackingFormFilter employeeTrackingFormFilter, Long userId) {
        User loggedUser=this.loggedUserService.getLoggedUser();
        String loggedUserEmail = loggedUser.getEmail();
        User user= this.userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        if(!user.getEmail().equals(loggedUserEmail)){
            throw new DifferentPrivilegeException();
        }
        if(privilegeService.loggedUserHasAnyPrivilege("HEAD_READ_DATA")) {
            List<EmployeeTrackingForm> list = new ArrayList<>();

            for(int i=0; i<loggedUser.getOrganizationalDepartmentUsers().size(); i++){
                if(loggedUser.getOrganizationalDepartmentUsers().get(i).getIsHead()){
                    list.addAll(this.employeeTrackingFormRepository.findAllByOrganizationalDepartmentId(employeeTrackingFormFilter, loggedUser.getOrganizationalDepartmentUsers().get(i).getOrganizationalDepartment().getId()));
                }
            }

            List<EmployeeTrackingForm> allCorrespondingHead = this.employeeTrackingFormRepository.findAllByUser(employeeTrackingFormFilter, loggedUser.getId());
            for (var t:allCorrespondingHead) {
                if(!list.contains(t)){
                    list.add(t);
                }
            }
            List<EmployeeTrackingForm> createdByUser = this.employeeTrackingFormRepository.findAllCreatedByUser(employeeTrackingFormFilter, loggedUser.getId());
            for (var t:createdByUser) {
                if(!list.contains(t)){
                    list.add(t);
                }
            }
            return list;
        } else {
            return null;
        }
    }


    @Override
    public List<EmployeeTrackingForm> findAllFormsForUserPaginated(String status, EmployeeTrackingFormFilter employeeTrackingFormFilter, Long userId, Pageable pageable) {
        User loggedUser=this.loggedUserService.getLoggedUser();
        String loggedUserEmail = loggedUser.getEmail();
        User user= this.userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        List<Long> organizationalUnitsForHead = new ArrayList<>();
        if(!user.getEmail().equals(loggedUserEmail)){
            throw new DifferentPrivilegeException();
        }
        if(privilegeService.loggedUserHasAnyPrivilege("HEAD_READ_DATA")) {
            List<EmployeeTrackingForm> list = new ArrayList<>();

            for(int i=0; i<loggedUser.getOrganizationalDepartmentUsers().size(); i++){
                if(loggedUser.getOrganizationalDepartmentUsers().get(i).getIsHead()){
                    organizationalUnitsForHead.add(loggedUser.getOrganizationalDepartmentUsers().get(i).getOrganizationalDepartment().getId());
                }
            }
            list.addAll(this.employeeTrackingFormRepository.findAllByOrganizationalDepartmentIdPaginated(status, loggedUser.getId(), employeeTrackingFormFilter, organizationalUnitsForHead, pageable).toList());
            return list;
        }
        return null;

    }

    private void createCell(Row row, int columnCount, Object value) {
        sheet.autoSizeColumn(columnCount);
        sheet.setColumnWidth(columnCount, 25 * 256);
        Cell cell = row.createCell(columnCount);
        cell.getRow().setHeightInPoints(cell.getSheet().getDefaultRowHeightInPoints() * 2);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof OffsetDateTime){
            cell.setCellValue(String.valueOf(value));
        } else {
            cell.setCellValue((String) value);
        }
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);
        row.cellIterator().forEachRemaining(cell1->cell1.setCellStyle(style));
    }


    private void writeHeaderLine() throws IOException {
        workbook=new XSSFWorkbook();
        sheet = workbook.createSheet("EmployeeTrackingForms");

        Row row = sheet.createRow(0);


        createCell(row, 0, "Работна задача");
        createCell(row, 1, "Наслов");
        createCell(row, 2, "Опис");
        createCell(row, 3, "Име и Презиме");
        createCell(row, 4, "Почетен датум");
        createCell(row, 5, "Краен датум");
        createCell(row, 6, "Статус");

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex());
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(style));
    }

    private void writeDataLines(EmployeeTrackingFormFilter employeeTrackingFormFilter, Boolean forDone) {
        int rowCount = 1;

        User user = this.loggedUserService.getLoggedUser();
        List<EmployeeTrackingForm> employeeTrackingForms = new ArrayList<>();

        if (privilegeService.loggedUserHasAnyPrivilege("ACCESS_ALL")) {
            employeeTrackingForms = this.employeeTrackingFormRepository.findAllCustom(employeeTrackingFormFilter);
        } else if (privilegeService.loggedUserHasAnyPrivilege("READ_USER_DATA")) {
            employeeTrackingForms = this.employeeTrackingFormRepository.findAllByUser(employeeTrackingFormFilter, user.getId());
        } else {
            employeeTrackingForms = findAllFormsForUser(employeeTrackingFormFilter,user.getId());
        }
        if(forDone){
            Status status = this.employeeTrackingFormStatusService.findByName("done");
            employeeTrackingForms.removeIf(t -> !t.getStatus().getLabel().equals(status.getLabel()));

        }else{
            Status status = this.employeeTrackingFormStatusService.findByName("in_progress");
            employeeTrackingForms.removeIf(t -> !t.getStatus().getLabel().equals(status.getLabel()));
        }

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);
        for (EmployeeTrackingForm employeeTrackingForm : employeeTrackingForms) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            if(employeeTrackingForm.getOrganizationalDepartmentWorkingItem()!=null &&employeeTrackingForm.getOrganizationalDepartmentWorkingItem().getWorkingItem()!=null){
                    createCell(row, columnCount++, employeeTrackingForm.getOrganizationalDepartmentWorkingItem().getWorkingItem().getName());
                    createCell(row, columnCount++, employeeTrackingForm.getTitle());
                    createCell(row, columnCount++, employeeTrackingForm.getDescription());
                    createCell(row, columnCount++, employeeTrackingForm.getUser().getPerson().getFirstName() +" "+employeeTrackingForm.getUser().getPerson().getLastName());
                    if(employeeTrackingForm.getTaskStartDate()!=null){
                        createCell(row, columnCount++, employeeTrackingForm.getTaskStartDate().toString());
                    }else{
                        createCell(row, columnCount++, "");
                    }
                    if(employeeTrackingForm.getTaskEndDate()!=null){
                        createCell(row, columnCount++, employeeTrackingForm.getTaskEndDate().toString());
                    }else{
                        createCell(row, columnCount++, "");
                    }
                    createCell(row, columnCount, employeeTrackingForm.getStatus().getLabel());
                    row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(style));
            }
        }
    }


    @Override
    public byte[] export(HttpServletResponse response, EmployeeTrackingFormFilter employeeTrackingFormFilter, Boolean forDone) throws IOException {
        if(employeeTrackingFormFilter.getStartDate()!=null && employeeTrackingFormFilter.getStartDate().getMinute()!=0){
            OffsetDateTime newStartDate = setOffsetDateTimeToZero(employeeTrackingFormFilter.getStartDate());
            employeeTrackingFormFilter.setStartDate(newStartDate);
        }

        if(employeeTrackingFormFilter.getEndDate()!=null && employeeTrackingFormFilter.getEndDate().getMinute()!=0){
            OffsetDateTime newEndDate = setOffsetDateTimeToZero(employeeTrackingFormFilter.getEndDate());
            employeeTrackingFormFilter.setEndDate(newEndDate);
        }

        writeHeaderLine();
        writeDataLines(employeeTrackingFormFilter, forDone);

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

        workbook.write(byteOutputStream);

        workbook.close();

        return byteOutputStream.toByteArray();
    }

    @Override
    public Optional<EmployeeTrackingForm> findById(Long id) {
        return this.employeeTrackingFormRepository.findById(id);
    }

    @Override
    public void closeTask(Long id) {
        EmployeeTrackingForm employeeTrackingForm = this.employeeTrackingFormRepository.getById(id);

        Status status = this.employeeTrackingFormStatusService.findByName("done");

        if(employeeTrackingForm.getTaskEndDate()==null){
            employeeTrackingForm.setStatus(status);
            LocalDateTime now = LocalDateTime.now();
            ZoneId zone = ZoneId.of("Europe/Berlin");
            ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
            OffsetDateTime currentTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0), zoneOffSet);
            employeeTrackingForm.setTaskEndDate(currentTime);
        }else{
            employeeTrackingForm.setStatus(status);
        }
        this.employeeTrackingFormRepository.save(employeeTrackingForm);

    }

    @Override
    public List<EmployeeTrackingForm> findAllByOrganizationalDepartmentId(Long id) {
        return this.employeeTrackingFormRepository.findAllByOrganizationalDepartmentId(new EmployeeTrackingFormFilter(), id);
    }

    @Override
    public List<EmployeeTrackingForm> findAllCreatedByUser(Long id) {
        return this.employeeTrackingFormRepository.findAllCreatedByUser(new EmployeeTrackingFormFilter(), id);
    }

    @Override
    public OffsetDateTime setOffsetDateTimeToZero(OffsetDateTime startDate) {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("Z");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);

        LocalDate temp = startDate.toLocalDate().minusDays(1);
        LocalTime time = LocalTime.of(23,0,0, 0);
        return OffsetDateTime.of(temp, time, zoneOffSet);

    }

    @Override
    public List<EmployeeTrackingForm> findAllWhichAreNotInHeadOrganizationalDepartment(Long userId, List<Long> organizationalDepartmentIds) {
        return this.employeeTrackingFormRepository.findAllWhichAreNotInHeadOrganizationalDepartment(userId, organizationalDepartmentIds);
    }

    @Override
    public void updateClosedTask(Long id) {
        EmployeeTrackingForm employeeTrackingForm = this.employeeTrackingFormRepository.findTimeTrackingFormByIdAndStatus(id,"done");
        Status status = this.employeeTrackingFormStatusService.findByName("in_progress");
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("Europe/Berlin");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
        OffsetDateTime currentTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0), zoneOffSet);
        employeeTrackingForm.setTaskStartDate(currentTime);
        employeeTrackingForm.setTaskEndDate(null);
        employeeTrackingForm.setStatus(status);
        employeeTrackingFormRepository.save(employeeTrackingForm);

    }
}
