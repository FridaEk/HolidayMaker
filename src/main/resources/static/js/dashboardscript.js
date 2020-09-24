const baseURL = "http://localhost:8080/api";
//document.addEventListener("DOMContentLoaded", listings(), false);

var user = JSON.parse(localStorage.getItem('user'));
var selectedBooking;
fetch("http://localhost:8080/api/booking?id=" + user.id)
  .then(response => response.json())
  .then(function(data) {
    console.log(data);

    var leftContainer = document.getElementsByClassName('left-container')[0];

    for (var i = 0; i < data.length; i++) {

      var bookingContainer = document.createElement('div');
      bookingContainer.classList.add('booking-container');
      bookingContainer.setAttribute("id", "B" + data[i].id);

      var resultBookingLeft = document.createElement('div');
      resultBookingLeft.classList.add("left-inner");

      var bookingId = document.createElement('p');
      bookingId.classList.add('style-p');
      bookingId.innerHTML = "Booking ID: " + data[i].id;

      var price = document.createElement('p');
      price.classList.add('style-p');
      price.innerHTML = "Cost: " + data[i].cost;

      var dateFrom = document.createElement('p');
      dateFrom.classList.add('style-p');
      dateFrom.innerHTML = "Date from: " + data[i].dateFrom;

      var dateTo = document.createElement('p');
      dateTo.classList.add('style-p');
      dateTo.innerHTML = "Date from: " + data[i].dateTo;

      var resultBookingRight = document.createElement('div');
      resultBookingRight.classList.add("right-inner");

      var rooms = document.createElement('p');
      rooms.classList.add('style-p');
      rooms.innerHTML = "Number of rooms: " + data[i].rooms;

      var adults = document.createElement('p');
      adults.classList.add('style-p');
      adults.innerHTML = "Number of adults: " + data[i].numberOfAdults;

      var kids = document.createElement('p');
      kids.classList.add('style-p');
      kids.innerHTML = "Number of kids: " + data[i].numberOfKids;

      var btnDiv = document.createElement('div');
      btnDiv.classList.add('buttons');

      var buttonEdit = document.createElement('input');
      buttonEdit.setAttribute("type", "button");
      buttonEdit.setAttribute("value", "Edit booking");

      buttonEdit.addEventListener("click", function(index) {
        return function() {
          editBooking(index);
        }
      }(i));

      var buttonDelete = document.createElement('input');
      buttonDelete.setAttribute("type", "button");
      buttonDelete.setAttribute("id", data[i].id);
      buttonDelete.setAttribute("value", "Delete booking");

      buttonDelete.addEventListener("click", function(index) {
        return function() {
          deleteBooking(index);
        }
      }(i));

      btnDiv.appendChild(buttonEdit);
      btnDiv.appendChild(buttonDelete);

      resultBookingLeft.appendChild(bookingId);
      resultBookingLeft.appendChild(price);
      resultBookingLeft.appendChild(dateFrom);
      resultBookingLeft.appendChild(dateTo);

      resultBookingRight.appendChild(rooms);
      resultBookingRight.appendChild(adults);
      resultBookingRight.appendChild(kids);

      bookingContainer.appendChild(resultBookingLeft);
      bookingContainer.appendChild(resultBookingRight);
      bookingContainer.appendChild(btnDiv);

      leftContainer.appendChild(bookingContainer);

    }

    function deleteBooking(index) {
      selectedBooking = data[index];
      fetch('http://localhost:8080/api/booking?id=' + selectedBooking.id, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json'
          }
        }).then(response => response.json())
        .then(data => {
          if (data == true || data == "true") {
            console.log('Success:', data);
            var b = document.getElementById("B" + selectedBooking.id);
            b.innerHTML = '';
          } else {
            console.log('Failure:', data);
          }
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    }

    function editBooking(index) {
      selectedBooking = data[index];

      document.getElementById('panel').style.display = "block";
      document.getElementById('formCover').style.display = "block";

      var bookingId = document.getElementById('booking-id');
      bookingId.innerHTML = "Booking ID: " + selectedBooking.id;

      var price = document.getElementById('price');
      price.innerHTML = "Cost: " + selectedBooking.cost;

      var dateFrom = document.getElementById('date-from');
      dateFrom.innerHTML = "Date from: " + selectedBooking.dateFrom;

      var dateTo = document.getElementById('date-to');
      dateTo.innerHTML = "Date to: " + selectedBooking.dateTo;

      var rooms = document.getElementById('rooms');
      rooms.innerHTML = "Number of rooms: " + selectedBooking.rooms;

      var adults = document.getElementById('adults');
      adults.innerHTML = "Number of adults: " + selectedBooking.numberOfAdults;

      var kids = document.getElementById('kids');
      kids.innerHTML = "Number of kids: " + selectedBooking.numberOfKids;

      var package = document.getElementById('package-selector');

      var packOpt1 = document.createElement('option');
      packOpt1.innerHTML = "Full Board";

      var packOpt2 = document.createElement('option');
      packOpt2.innerHTML = "Half Board";

      var packOpt3 = document.createElement('option');
      packOpt3.innerHTML = "All Inclusive";


      if (selectedBooking.fullBoard == true) {
        packOpt1.selected = true;
      }

      if (selectedBooking.halfBoard == true) {
        packOpt2.selected = true;
      }

      if (selectedBooking.allInclusive == true) {
        packOpt3.selected = true;
      }

      package.add(packOpt1);
      package.add(packOpt2);
      package.add(packOpt3);

      var extraBed = document.getElementById('bed-selector');

      var bedOpt1 = document.createElement('option');
      bedOpt1.value = true;
      bedOpt1.innerHTML = "Yes";

      var bedOpt2 = document.createElement('option');
      bedOpt2.value = false;
      bedOpt2.innerHTML = "No";

      if (selectedBooking.extraBed == true)
        bedOpt1.selected = true;
      if (selectedBooking.extraBed == false)
        bedOpt2.selected = true;

      extraBed.add(bedOpt1);
      extraBed.add(bedOpt2);
    }
  });

var updateBtn = document.getElementById('update');
updateBtn.addEventListener('click', function() {

  var package = document.getElementById('package-selector');
  var extraBed = document.getElementById('bed-selector');

  selectedBooking.extraBed = (extraBed.value === 'true');
  selectedBooking.fullBoard = (package.value === 'Full Board');
  selectedBooking.halfBoard = (package.value === 'Half Board');
  selectedBooking.allInclusive = (package.value === 'All Inclusive');

  putData("http://localhost:8080/api/booking?id=" + selectedBooking.id, selectedBooking)
    .then(location.reload());
})

async function putData(url, data) {
  // Default options are marked with *
  const response = await fetch(url, {
    method: 'PUT',
    mode: 'cors',
    cache: 'no-cache',
    credentials: 'same-origin',
    headers: {
      'Content-Type': 'application/json'
    },
    redirect: 'follow',
    referrerPolicy: 'no-referrer',
    body: JSON.stringify(data)
  });
  return response.json();
}

listings();

function listings() {
  fetch(baseURL + "/accommodation/customer?id=" + user.id)
    .then(response => response.json())
    .then(function(data) {

      var rightContainer = document.getElementsByClassName('right-container')[0];

      for (var i = 0; i < data.length; i++) {
        var listingContainer = document.createElement('div');
        listingContainer.classList.add('listing-container');
        listingContainer.setAttribute("id", "L" + data[i].id);

        var resultListingLeft = document.createElement('div');
        resultListingLeft.classList.add('column-left');

        var listingId = document.createElement('p');
        listingId.classList.add('style-p');
        listingId.innerHTML = "Listing ID: " + data[i].id;

        var listingLocation = document.createElement('p');
        listingLocation.classList.add('style-p');
        console.log(data[i].location);
        listingLocation.innerHTML = "Location: " + data[i].location.name;

        var listingRooms = document.createElement('p');
        listingRooms.classList.add('style-p');
        console.log(data[i].rooms);
        listingRooms.innerHTML = "Number of rooms: " + data[i].rooms.size;

        var rating = document.createElement('p');
        rating.classList.add('style-p');
        rating.innerHTML = "Rating: " + data[i].rating;

        var resultListingRight = document.createElement('div');
        resultListingRight.classList.add('column-right');

        var pool = document.createElement('p');
        pool.classList.add('style-p');
        pool.innerHTML = "Pool: " + (data[i].pool ? 'Yes' : 'No')

        var beach = document.createElement('p');
        beach.classList.add('style-p');
        beach.innerHTML = "Distance to beach: " + data[i].distanceToBeach;

        var center = document.createElement('p');
        center.classList.add('style-p');
        center.innerHTML = "Distance to center: " + data[i].distanceToCenter;

        var btnDiv = document.createElement('div');
        btnDiv.classList.add('buttons');

        var listingBtn = document.createElement('input');
        listingBtn.setAttribute("type", "button");
        listingBtn.setAttribute("id", data[i].id);
        listingBtn.setAttribute("value", "Delete listing");
        listingBtn.setAttribute("onclick", "deleteListing(this)");

        btnDiv.appendChild(listingBtn);

        resultListingLeft.appendChild(listingId);
        resultListingLeft.appendChild(listingLocation);
        resultListingLeft.appendChild(listingRooms);
        resultListingLeft.appendChild(rating);

        resultListingRight.appendChild(pool);
        resultListingRight.appendChild(beach);
        resultListingRight.appendChild(center);

        listingContainer.appendChild(resultListingLeft);
        listingContainer.appendChild(resultListingRight);
        listingContainer.appendChild(btnDiv);

        rightContainer.appendChild(listingContainer);
      }
    });
}

function addListingForm() {
  //  document.getElementById("add-listing-container").style.display = "block";
    var addListingContainer = document.getElementById('add-listing-container')[0];

    var listingInfo = document.createElement('div');
    listingInfo.classList.add("listing-info");


    var name = document.createElement('label');
    name.classList.add('listing-title');
    name.innerHTML = "Name";
    var nameInput = document.createElement('input');
    nameInput.setAttribute("type", "text");

    listingInfo.appendChild(nameInput);
    listingInfo.appendChild(name);

    addListingContainer.appendChild(listingInfo);


}

function addListing() {

}

function deleteListing(element) {
  fetch(baseURL + "/accommodation?id=" + element.id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    .then(response => response.json())
    .then(data => {
      if (data == true || data == "true") {
        console.log('Success:', data);
        var listing = document.getElementById("L" + element.id);
        listing.innerHTML = '';
      } else {
        console.log('Failure:', data);
      }
    })
    .catch((error) => {
      console.error('Error:', error);
    });
}
