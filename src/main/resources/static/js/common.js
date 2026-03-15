const getHTTPService = (url, type, dataType) => {
    $.ajax({
        url: url,
        type: type,
        dataType: dataType,
        async: false,
    })
    .done(function (data, jqXHR) {
        console.log("Data fetched successfully:", data);

        return data;
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
        console.error("Error fetching data:", textStatus, errorThrown);
        console.error("Response text:", jqXHR.responseText);
        console.error("Status code:", jqXHR.status);
        alert("Error: " + textStatus);
        return textStatus;
    })
    .always(function () {
        console.log("Request complete");
    });
}