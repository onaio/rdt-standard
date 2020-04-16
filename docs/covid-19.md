# Roadmap for Covid-19 Flavor

## Release 1:

1. Update Data Dictionary COVID-19 Data Dictionary
    1. Get stakeholder approval on data dictionary
    1. Translate to Bahasa
1.	Set up instance of OpenMRS to derive serialized barcodes and users (1 day to set up OpenMRS instance)
    1.	Determine number of barcodes needed for initial request
        1.	Use OpenMRS id-gen module to generate unique rdt IDs
    1.	Determine number of users needed
        1.	Will multiple health care workers support patients or will it be 1 HW to 1 patient?
1.	Review server to verify capacity for 10,000+ submissions per day and 6,000+ swabs through laboratory lifecycle (1 week with Jason’s Help)
    1.	Get buy in on size requirement, existing malaria study was 4GB
    1.	Get buy in from SRE team
1.	Update template images in the library  (UW completed already)
    1.	[Initial template images](https://cs.washington.edu/rdt-scan)
        1.	Wondwo (1 test line) nCoV-2019 IgG/IgM
        1.	ALLTEST Accurate nCoV-2019 IgG/IgM
    1.	Merge template library to rdt repo
1.	Update Forms in OpenSRP
    1.	Update Patient Questionnaire with RDT Dictionary (Patient Form)
    1.	Update Test Form
1.	Update Design with new user workflow
    1.	Update patient registration page
    1.	Update job aids to associate with RDT chosen/scanned - TBD we can have the option of “Scan Barcode” and “Select RDT Manually”. If we want to ensure barcodes are used, then only Scan barcode should be done.
        1.	AllTests
        1.	Wondfo
    1.	Image confirmation screen - make the health worker ensure good quality image has been taken before saving the image.
        1.	Reduces errors in image capture
    1.	Health worker choice screen
        1.	Test result interpretation screen with health worker choices (Positive, Negative, Invalid) specific to the RDT
        1. ![test options](https://raw.githubusercontent.com/onaio/rdt-standard/master/docs/assets/rdts.png)
    1.	Design the recommendation screen based on CHW results recorded
        1.	Get approval of recommendation list from Anu/IDI/Decision maker
    1.	Create a screen that asks if nasal swabs are being taken. (Yes/No)
    1.	Support Nasal Swab collection
        1.	Button for Scanning barcode
        1.	Button to skip collection
        1.	Directions on what to do and how to store the sample.
    1.	Integrate RDT image library
    1.	Screen to track multiple patient RDTs
        1.	Add patient name, timer countdown from manufacturers recommendations and a button to open the test where you last left (where the image capture screen)
    1.	Prior to Image Capture of RDT, the health worker needs to scan and confirm the barcode of RDT to ensure the correct RDT is being captured and assessed.
    1.	Design an end screen with instructions for storage and shipment
        1.	Could also add something for PPE instructions/safety/decontamination
1.	Application Build (NB: much of this is complete but needs review in application to new app flavor)
    1.	Setup OpenSRP instance in cloud host
        1.	Assess if storage in back-up location is needed
    1.	Implement Login screen
        1.	Implement English and Bahasa version on login screen
        1.	Get parties to translate required fields of code into Bahasa
        1.	Implement Sync button and toggle button to hamburger menu
    1.	Build Client Registration form
        1.	Client register activity
        1.	Client register fragment
        1.	Implement Recycler view holder
        1.	Implement presenter-view
        1.	Implement register fragment presenter and interactor
    1.	Implement client processor
    1.	Implement barcode scanning and tracking of RDT
        1.	Customize JSON form barcode widget to extract information from Zebra widget/api/application
        1.	Autoselect RDT type based on barcode information
        1.	Collect device properties
        1.	Collect phone and OS information
        1.	Track RDT capture activity response time
        1.	Implement expiration date confirmation from 2D barcode
        1.	Implement test type confirmation from 2D barcode
        1.	Save barcode information in OpenSRP
    1.	Build test form
    1.	Implement GPS capture
    1.	Update Job aid test procedures build to support RDTs available
        1.	Fork to accommodate the different RDTs and their protocols
    1.	Implement camera image-capture json form widget from UW repository
        1.	Implement server/client support for saving and syncing multiple images per patient
    1.	Implement Manual image capture
        1.	30 second timeout
        1.	Explore requirements for manual image capture improvement (Check for sharp/bright etc)
        1.	Build confirmation screen for health workers to confirm acceptable image was taken
        iv.	Implement image sync timeout and server kill switch
    1.	Assign timers for respective RDTs
        1.	Implement timers
        1.	Implement alarms and sounds
        1.	Implement test end timer based on manufacturer instructions and test reliability tolerance
    1.	Build screen to support nasal swab collection and 2D barcode scanning
        1.	Record nasal swab collection barcode information in OpenSRP
        1.	Update status of nasal swab to collected
            1.	Status available: Collected, Received at laboratory (Location?), Processing, Completed (positivity)
1.	Implement rdt test EC tables
1.	Enable exporting of RDT images saved on OpenSRP server
1.	Store result of RDT app algorithm
1.	Build a health worker recommendation screen using logic from RDT results stored.
1.	Support multiple RDT capture
a.	Build out screen and code to support multiple simultaneous patients
1.	Setup and Deploy production server for application

## Release 2
1.	Create WebUI for laboratory nasal swab collection/status updates/data upload
2.	Integrate Reveal platform
a.	TBD how this will be integrated (patient registration screen with map view?)
3.	Integrate TrueCover
a.	TBD on integration

## Backend

1.	Build Nifi connectors to transport data from laboratory UI to OpenSRP
1.	Build Nifi connectors to transport data from OpenSRP to Canopy Discover
1.	Build dashboards
    1.	Covid Community spread
    1.	Risk Areas
    1.	Hotspots
    1.	RDT sensitivity and specificity

## Ideas

1.	Confirm auto interpretation with user input or a flagging system for supervisor to check result outcome of RDT
