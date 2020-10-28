function onSubmit(e) {
  var data = {
      name: null,
      surname: null,
      purpose: null,
      email: null,
      phoneNumber: null,
      country: null,
      date: "2020-01-01",
      time: "00:00:00",
      peopleCount: 0,
      age: 0,
      ageGroups: ['YOUTH', 'JUNIOR_ADULTS', 'SENIOR_ADULTS', 'SENIORS'],
      languages: [],
      preferences: "",
      additionalPreferences: ""
  }
  var response = e.response;
  var itemResponses = response.getItemResponses();
  data.name = itemResponses[0].getResponse();
  data.surname = itemResponses[1].getResponse();
  data.purpose = toPurpose(itemResponses[2].getResponse());
  data.email = itemResponses[3].getResponse();
  data.phoneNumber = itemResponses[4].getResponse();
  data.country = itemResponses[5].getResponse();
  data.date = itemResponses[6].getResponse();
  data.time = itemResponses[7].getResponse() + ":00";
  data.peopleCount = itemResponses[8].getResponse();
  data.age = itemResponses[9].getResponse();
  var ageGroupsResponses = itemResponses[10].getResponse();
  var ageGroups = [];
  for (var j = 0; j < ageGroupsResponses.length; j++) {
    ageGroups.push(toAgeGroupName(ageGroupsResponses[j]));
  }
  data.ageGroups = ageGroups;
  var languagesResponses = itemResponses[11].getResponse();
  var languages = [];
  for (var j = 0; j < languagesResponses.length; j++) {
    languages.push(languagesResponses[j].toUpperCase());
  }
  data.languages = languages;
  if (itemResponses[12] != undefined) {
    var preferencesResponses = itemResponses[12].getResponse();
    var preferences = "";
    for (var j = 0; j < preferencesResponses.length; j++) {
        preferences += preferencesResponses[j];
        if (j + 1 < preferencesResponses.length)
          preferences += ", ";
    }
    data.preferences = preferences;
  }
  if (itemResponses[13] != undefined) {
    var additionalPreferencesResponse = itemResponses[14].getResponse();
    data.additionalPreferences = additionalPreferencesResponse;
  }

  var userEmail = "meetalocaltest@gmail.com";
  var subject = "Sent Meet a Local Form";
  var message = JSON.stringify(data);

  MailApp.sendEmail (userEmail, subject, message);

  var options = {
    'method' : 'post',
    'contentType': 'application/json',
    'payload' : JSON.stringify(data)
  };
  var url = "http://localhost:8080/registration/meets";
  UrlFetchApp.fetch(url, options);
}

function toAgeGroupName(ageGroup) {
  if (ageGroup == "18-29")
    return "YOUTH";
  else if (ageGroup == "30-39")
    return "JUNIOR_ADULTS";
  else if (ageGroup == "40-49")
    return "SENIOR_ADULTS";
  else
    return "SENIORS";
}

function toPurpose(purpose) {
  if (purpose == "Tourism")
    return "TOURISM";
  else
    return "RELOCATION";
}