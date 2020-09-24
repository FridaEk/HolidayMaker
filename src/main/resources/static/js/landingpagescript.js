


checkLoginTime();
toggleNavBarButtons();

function toggleNavBarButtons() {
    
    var loginBtn = document.getElementById('login');
    var signinBtn = document.getElementById('signup');
    var signoutBtn = document.getElementById('profile');
    var profileBtn = document.getElementById('signOut');
    
    if (localStorage.getItem('user') === null) {
        loginBtn.innerHTML = "<a class='nav-link' href='#' onclick='openLogin()'>Login</a>";
        signinBtn.innerHTML = "<a class='nav-link' href='#' onclick='openReg()'>Sign up</a>";
        signoutBtn.innerHTML = "";
        profileBtn.innerHTML = "";
    } else {
        loginBtn.innerHTML = "";
        signinBtn.innerHTML = "";
        signoutBtn.innerHTML = "<a class='nav-link' href='' tabindex='-1' onclick='signOut()' aria-disabled='true'>Sign out</a>";
        profileBtn.innerHTML = "<a class='nav-link' href='dashboard' tabindex='-1' aria-disabled='true'>Profile</a>";
    }
}

function signOut() {
  if (confirm("You're about to log out. Do you want to proceed?'")) {
      localStorage.removeItem('user');
      location.reload();
  }
}

function openLogin() {
  document.getElementById("formCover").style.display = "block";
  document.getElementById("loginForm").style.display = "flex";
}

async function closeLogin(flag) {

  if (flag === true) {
      var email = document.getElementById('email').value;
      var password = document.getElementById('password').value;
      var fetchUrl = "http://localhost:8080/api/login?email=" + email + "&password=" + password;
    
    
      await fetch(fetchUrl)
        .then(response => response.json())
        .then(function(data) {
          localStorage.setItem('user', JSON.stringify(data));
          var profile = document.getElementById("profile");
          profile.classList.remove("disabled");
        })
        .catch((error) => {
          console.log("No user")
        });
  }

  document.getElementById("formCover").style.display = "none";
  document.getElementById("loginForm").style.display = "none";

  location.reload();
}

function openReg() {
  document.getElementById("formCover").style.display = "block";
  document.getElementById("regForm").style.display = "flex";
}

async function closeReg(flag) {

  if (flag === true) {
  var email = document.getElementById('regEmail').value;
  var password = document.getElementById('regPass').value;
  var password2 = document.getElementById('regPassRep').value;

  var fetchUrl = "http://localhost:8080/api/customer";

  var newCustomer = {
    email: email,
    password: password
  };

  await fetch(fetchUrl, {
      method: 'POST', // or 'PUT'
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newCustomer),
    })
    .then(response => response.json())
    .then(function(data) {
      console.log(data);
    });
    
  }

  document.getElementById("formCover").style.display = "none";
  document.getElementById("regForm").style.display = "none";
}


var getDates = function(startDate, endDate) {
  var dates = [],
    currentDate = startDate,
    addDays = function(days) {
      var date = new Date(this.valueOf());
      date.setDate(date.getDate() + days);
      return date;
    };
  while (currentDate <= endDate) {
    dates.push(currentDate);
    currentDate = addDays.call(currentDate, 1);
  }
  return dates;
};


var checkInSelector = document.getElementById('checkInSelector');
var checkOutSelector = document.getElementById('checkOutSelector');

var dates = getDates(new Date(2021, 05, 1), new Date(2021, 06, 31));
dates.forEach(function(date) {
  var option1 = document.createElement("option");
  option1.text = ((date.getDate() < 10) ? '0' : '') + date.getDate() + "/" + ((date.getMonth() + 1 < 10) ? '0' : '') + (date.getMonth() + 1) + "/" + date.getFullYear();
  //option1.value = ((date.getDate() < 10) ? '0' : '') + date.getDate() + "-" + ((date.getMonth() + 1 < 10) ? '0' : '') + (date.getMonth() + 1) + "-" + date.getFullYear();
  checkInSelector.add(option1);
  var option2 = document.createElement("option");
  option2.text = ((date.getDate() < 10) ? '0' : '') + date.getDate() + "/" + ((date.getMonth() + 1 < 10) ? '0' : '') + (date.getMonth() + 1) + "/" + date.getFullYear();
  //option2.value = ((date.getDate() < 10) ? '0' : '') + date.getDate() + "-" + ((date.getMonth() + 1 < 10) ? '0' : '') + (date.getMonth() + 1) + "-" + date.getFullYear();
  checkOutSelector.add(option2);

});


var show = document.getElementById('show');
show.addEventListener('click', function() {
  var location = document.getElementById('locationSelector').value;
  var dateFrom = document.getElementById('checkInSelector').value;
  var dateTo = document.getElementById('checkOutSelector').value;
  var adults = document.getElementById('adultsSelector').value;
  var kids = document.getElementById('kidsSelector').value;
  var package = document.getElementById('packageSelector').value;
  var pool = document.getElementById('poolSelector').value;
  var entertainment = document.getElementById('entertainmentSelector').value;
  var restaurant = document.getElementById('restaurantSelector').value;
  var childAct = document.getElementById('childActivitiesSelector').value;


  var rooms = document.getElementById('roomsSelector').value;
  var center = document.getElementById('centerSelector').value;
  var beach = document.getElementById('beachSelector').value;


  var fetchUrl = "http://localhost:8080/api/accommodation/filter?location=" + location + "&dateFrom=" + dateFrom +
    "&dateTo=" + dateTo + "&pool=" + pool + "&childrenClub=" + childAct + "&restaurant=" + restaurant +
    "&nightEntertainment=" + entertainment + "&rooms=" + rooms + "&adults=" + adults + "&kids=" + kids +
    "&distToBeach=" + beach + "&distToCenter=" + center;

  onShow(fetchUrl)
});

function onShow(url) {
    
  var filterInputs = document.getElementsByTagName('select');
  fetch(url)
    .then(function(response) {
        if (response.status >= 400 && response.status < 600) {
            
            for (element of filterInputs) {
                if (element.value === "") {
                    element.style = "border: 1px solid red";
                }
            }
            
            throw new Error("Bad response from server");
        }
        
        return response.json();
    })
    .then(function(data) {

      for (element of filterInputs) {
            if (element.value === "") {
                element.style.border = "0px solid black";
                element.removeAttribute('style');
            }
      }

      var resultContainer = document.getElementsByClassName('resultsContainer')[0];
      var sorting = document.getElementById('sort');

      let bubbleSortAsc = (inputArr) => {
        let len = inputArr.length;
        for (let i = 0; i < len; i++) {
          for (let j = 0; j < len - 1; j++) {
            if (inputArr[j].rating > inputArr[j + 1].rating) {
              let tmp = inputArr[j];
              inputArr[j] = inputArr[j + 1];
              inputArr[j + 1] = tmp;
            }
          }
        }
        return inputArr;
      };

      let bubbleSortDesc = (inputArr) => {
        let len = inputArr.length;
        for (let i = 0; i < len; i++) {
          for (let j = 0; j < len - 1; j++) {
            if (inputArr[j].rating < inputArr[j + 1].rating) {
              let tmp = inputArr[j];
              inputArr[j] = inputArr[j + 1];
              inputArr[j + 1] = tmp;
            }
          }
        }
        return inputArr;
      };


      display();

      sorting.addEventListener("change", function() {
        display();
      });

      function display() {
        resultContainer.innerHTML = '';

        if (sorting.value === "asc")
          data = bubbleSortAsc(data);
        else
          data = bubbleSortDesc(data);

        for (var i = 0; i < data.length; i++) {

          var resultItem = document.createElement('div');
          resultItem.classList.add("resultItem");

          var itemImg = document.createElement('div');
          itemImg.classList.add("itemImg");

          var itemTitle = document.createElement('p');
          itemTitle.classList.add('itemTitle');
          itemTitle.innerHTML = data[i].name;

          var itemRooms = document.createElement('p');
          itemRooms.classList.add('itemRooms');
          itemRooms.innerHTML = data[i].rooms.length + " rooms";

          var rowPairContainer1 = document.createElement('div');
          rowPairContainer1.classList.add("rowPairContainer");

          var rowPairContainer2 = document.createElement('div');
          rowPairContainer2.classList.add("rowPairContainer");

          var itemPool = document.createElement('p');
          itemPool.classList.add('itemPool');
          itemPool.classList.add('halfContainer');
          itemPool.innerHTML = (data[i].pool ? 'Pool' : 'No Pool');

          var itemChildren = document.createElement('p');
          itemChildren.classList.add('itemChildren');
          itemChildren.classList.add('halfContainer');
          itemChildren.innerHTML = (data[i].childEvents ? 'Children Activities' : 'No Children Activities');

          var itemEntertainment = document.createElement('p');
          itemEntertainment.classList.add('itemEntertainment');
          itemEntertainment.classList.add('halfContainer');
          itemEntertainment.innerHTML = (data[i].nightEntertainment ? 'Entertainment' : 'No Entertainment');

          var itemRestaurant = document.createElement('p');
          itemRestaurant.classList.add('itemRestaurant');
          itemRestaurant.classList.add('halfContainer');
          itemRestaurant.innerHTML = (data[i].restaurant ? 'Restaurant' : 'No Restaurant');

          var itemDesc = document.createElement('p');
          itemDesc.classList.add('itemDesc');
          itemDesc.innerHTML = data[i].description.substring(0, 140) + " ...";


          var button = document.createElement('input');
          button.setAttribute("type", "button");
          button.setAttribute("value", "Inspect");
          button.disabled = (localStorage.getItem('user') == null) ? true : false;

          button.addEventListener("click", function(index) {
            return function() {
              setAccDetails(index);
            }
          }(i));

          rowPairContainer1.appendChild(itemPool);
          rowPairContainer1.appendChild(itemChildren);


          rowPairContainer2.appendChild(itemEntertainment);
          rowPairContainer2.appendChild(itemRestaurant);


          resultItem.appendChild(itemImg);
          resultItem.appendChild(itemTitle);
          resultItem.appendChild(itemRooms);
          resultItem.appendChild(rowPairContainer1);
          resultItem.appendChild(rowPairContainer2);
          resultItem.appendChild(itemDesc);
          resultItem.appendChild(button);

          resultContainer.appendChild(resultItem);
        }
      }

      function setAccDetails(index) {

        var dateFrom = document.getElementById('checkInSelector').value;
        var dateTo = document.getElementById('checkOutSelector').value;
        var adults = document.getElementById('adultsSelector').value;
        var kids = document.getElementById('kidsSelector').value;
        var package = document.getElementById('packageSelector').value;

        collected = {
          id: data[index].id,
          dateFrom: dateFrom,
          dateTo: dateTo,
          adults: adults,
          kids: kids,
          package: package
        };

        localStorage.setItem('selectAcc', JSON.stringify(collected));

        window.location.href = '/accommodation';
      }
    }).catch(function(error) {
        console.log(error);
    });;

}


fetch("http://localhost:8080/api/locations")
  .then(response => response.json())
  .then(function(data) {

    var locationSelector = document.getElementById('locationSelector');

    for (var i = 0; i < data.length; i++) {
      var option = document.createElement("option");
      option.text = data[i].name;
      locationSelector.add(option);
    }

    var adultsSelector = document.getElementById('adultsSelector');
    for (var i = 1; i <= 50; i++) {
      var option = document.createElement("option");
      option.text = i;
      adultsSelector.add(option);
    }


    var kidsSelector = document.getElementById('kidsSelector');
    for (var i = 0; i <= 50; i++) {
      var option = document.createElement("option");
      option.text = i;
      kidsSelector.add(option);
    }

    var roomsSelector = document.getElementById('roomsSelector');
    for (var i = 1; i <= 50; i++) {
      var option = document.createElement("option");
      option.text = i;
      roomsSelector.add(option);
    }

    var packageSelector = document.getElementById('packageSelector');
    var full = document.createElement("option");
    full.text = "Full Board";

    var half = document.createElement("option");
    half.text = "Half Board";

    var all = document.createElement("option");
    all.text = "All Inclusive";

    packageSelector.add(all);
    packageSelector.add(full);
    packageSelector.add(half);

    var centerSelector = document.getElementById('centerSelector');
    for (var i = 500; i <= 5000; i += 500) {
      var option = document.createElement("option");
      option.text = '<' + i + 'm';
      option.value = i;
      centerSelector.add(option);
    }

    var beachSelector = document.getElementById('beachSelector');
    for (var i = 500; i <= 5000; i += 500) {
      var option = document.createElement("option");
      option.text = '<' + i + 'm';
      option.value = i;
      beachSelector.add(option);
    }

  });