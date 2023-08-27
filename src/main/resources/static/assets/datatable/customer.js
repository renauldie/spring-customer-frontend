$(document).ready(() => {
    $('#data').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": "/customers/",
            "type": "POST",
            "dataType": "json",
            "contentType": "application/json",
            "data": function (d) {
                return JSON.stringify(d);
            }
        },
        "columns": [
            {
                data: null,
                render: (data, type, row, meta) => {
                    return meta.row + 1;
                },
            },
            {data: "nama", width: "20%"},
            {data: "alamat", width: "20%"},
            {data: "kota", width: "20%"},
            {data: "provinsi", width: "20%"},
            {data: "tgl_registrasi", "width": "20%"},
            {data: "status", width: "20%"},
            {
                data: null,
                render: function (data, type, row, meta) {
                    return `
            <button
              type="button"
              class="btn btn-warning"
              data-toggle="modal"
              data-target="#customer-update"
              onclick="show(${data.id})"
            >
              Update
            </button>
            <button
              type="button"
              class="btn btn-danger"
              onclick="remove(${data.id})"
            >
              Delete
            </button>
          `;
                },
            },
        ]
    });

    $("#updateForm").submit(function(event) {
        event.preventDefault();

        const formData = [
            {path: '/name', value: $("#update-name").val(), op: 'replace'},
            {path: '/address', value: $("#update-address").val(), op: 'replace'},
            {path: '/city', value: $("#update-city").val(), op: 'replace'},
            {path: '/province', value: $("#segment-province").val(), op: 'replace'},
            {path: '/status' ,value: $("#update-status").val(), op: 'replace'}
        ];
        const id = $("#update-id").val()
        update(formData, id);
    });
});

function show(id) {
    $.ajax({
        method: "GET",
        url: "/customers/" + id,
        dataType: "JSON",
        // contentType: "application/json",
        success: (res) => {
            const customRegsDate = res.registerDate;
            const resultRegsDate = new Date(customRegsDate);
            const formattedRegsDate = formatDate(resultRegsDate);

            $("#update-id").val(res.id);
            $("#update-name").val(res.name);
            $("#update-address").val(res.address);
            $("#update-city").val(res.city);
            $("#update-province").val(res.province);
            $("#update-register-date").val(formattedRegsDate);
            $("#update-status").val(res.status);
        },
    });
}

function create() {
    let name = $("#create-name").val();
    let address = $("#create-address").val();
    let city = $("#create-city").val();
    let province = $("#create-province").val();
    let status = $("#create-status").val();

    $.ajax({
        method: "POST",
        url: "/customers/create",
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            address: address,
            city: city,
            province: province,
            status: status,
        }),
        success: (res) => {
            $("#create").modal("hide");
            $("#data").DataTable().ajax.reload();
        },
    });
}

function update(formData, id) {
    console.log(formData);
    $.ajax({
        contentType: "application/json",
        data: JSON.stringify(formData),
        dataType: "json",
        method: "PATCH",
        url: "/customers/update/" + id,
        success: (res) => {
            $("#update").modal("hide");
            $("#data").DataTable().ajax.reload();
        },
    });
}

function remove(id) {
    if (confirm("Are you sure want to delete this Customer?")) {
        $.ajax({
            method: "DELETE",
            url: `/customers/${id}`,
            dataType: "JSON",
            contentType: "application/json",
            success: (res) => {
                $("#data").DataTable().ajax.reload();
            },
        });
    }
}

function formatDate(date) {
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    return year + '-' + month + '-' + day;
}