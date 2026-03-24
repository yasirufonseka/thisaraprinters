function openTab(evt, tabName) {
    // hide all content boxes
    var i, tabContent, tabButtons;
    tabContent = document.getElementsByClassName("tab-content");
    for (i = 0; i < tabContent.length; i++) {
        tabContent[i].style.display = "none";
    }

    // remove active class from all buttons
    tabButtons = document.getElementsByClassName("tab-btn");
    for (i = 0; i < tabButtons.length; i++) {
        tabButtons[i].className = tabButtons[i].className.replace(" active", "");
    }

    // show clicked content box and add active class to clicked button
    document.getElementById(tabName).style.display = "block";
    if (evt) {
        evt.currentTarget.className += " active";
    }
}

// Default open tab (optional, can also be handled by HTML style="display:block")
document.addEventListener("DOMContentLoaded", function () {
    var defaultBtn = document.querySelector(".tab-btn");
    if (defaultBtn) {
        defaultBtn.click();
    }
});
