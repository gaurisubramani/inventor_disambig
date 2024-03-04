//PATENT
use ${rawdata}2014/application_data, clear
drop if application_type !="REGULAR" // Change for cross applications
rename invention_title title
gen uuid = _n
keep application_number title uuid filing_date
order application_number title uuid filing_date
save ${filedata}disambiguation_files/patent_full, replace
export delimited ${filedata}disambiguation_files/export/patent_full.tsv, replace

//RAWASSIGNEE
use ${rawdata}assignment_data/2014/documentid_admin.dta, clear
sort rf_id
joinby rf_id using ${rawdata}assignment_data/2014/assignee.dta
rename appno_doc_num application_number
rename ee_name organization 
keep application_number organization
//merge
sort application_number
merge m:1 application_number using ${filedata}disambiguation_files/patent_full
drop if _merge != 3
drop _merge
keep application_number organization
gen uuid = _n
save ${filedata}disambiguation_files/rawassignee_full, replace
export delimited ${filedata}disambiguation_files/export/rawassignee_full.tsv, replace

//RAWINVENTOR
use ${rawdata}2014/all_inventors
keep application_number inventor_name_first inventor_name_middle inventor_name_last
rename inventor_name_first name_first 
rename inventor_name_middle name_middle 
rename inventor_name_last name_last
//merge
merge m:1 application_number using ${filedata}disambiguation_files/patent_full
drop if _merge != 3
drop _merge
keep application_number name_first name_middle name_last
gen uuid = _n
save ${filedata}disambiguation_files/rawinventor_full, replace
export delimited ${filedata}disambiguation_files/export/rawinventor_full.tsv, replace

//RAWLOCATION
use ${rawdata}2014/all_inventors
keep application_number inventor_region_code inventor_country_code inventor_country_name
rename inventor_region_code state
rename inventor_country_code country_code
rename inventor_country_name country
//merge
merge m:1 application_number using ${filedata}disambiguation_files/patent_full
drop if _merge != 3
drop _merge
keep application_number state country_code country
gen uuid = _n
save ${filedata}disambiguation_files/rawlocation_full, replace
export delimited ${filedata}disambiguation_files/export/rawlocation_full.tsv, replace

//RAWLAWYER
use ${rawdata}2014/correspondence_address, clear
keep application_number correspondence_name_line_1 correspondence_name_line_2 correspondence_street_line_1 correspondence_street_line_2 correspondence_city correspondence_postal_code correspondence_region_code correspondence_country_code correspondence_country_name
//merge
merge m:1 application_number using ${filedata}disambiguation_files/patent_full
drop if _merge != 3
drop _merge
keep application_number correspondence_name_line_1 correspondence_name_line_2 correspondence_street_line_1 correspondence_street_line_2 correspondence_city correspondence_postal_code correspondence_region_code correspondence_country_code correspondence_country_name
gen uuid = _n
save ${filedata}disambiguation_files/rawlawyer_full, replace
export delimited ${filedata}disambiguation_files/export/rawlawyer_full.tsv, replace

//USPC_CURRENT
use ${rawdata}2014/application_data, clear
drop if application_type !="REGULAR" // Change for cross applications
rename uspc_class mainclass_id
rename uspc_subclass subclass_id
keep application_number mainclass_id subclass_id
merge m:1 application_number using ${filedata}disambiguation_files/patent_full
drop if _merge != 3
drop _merge
keep application_number mainclass_id subclass_id
gen uuid = _n
save ${filedata}disambiguation_files/uspc_current_full, replace
export delimited ${filedata}disambiguation_files/export/uspc_current_full.tsv, replace
