function onSubmit(e) {
  var data = {
      name: null,
      surname: null,
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
  data.email = itemResponses[2].getResponse();
  data.phoneNumber = itemResponses[3].getResponse();
  data.country = itemResponses[4].getResponse();
  data.date = itemResponses[5].getResponse();
  data.time = itemResponses[6].getResponse() + ":00";
  data.peopleCount = itemResponses[7].getResponse();
  data.age = itemResponses[8].getResponse();
  var ageGroupsResponses = itemResponses[9].getResponse();
  if (ageGroupsResponses != null) {
    var ageGroups = [];
    for (var j = 0; j < ageGroupsResponses.length; j++) {
      ageGroups.push(toAgeGroupName(ageGroupsResponses[j]));
    }
    data.ageGroups = ageGroups;
  }
  var languagesResponses = itemResponses[10].getResponse();
  var languages = [];
  for (var j = 0; j < languagesResponses.length; j++) {
    languages.push(languagesResponses[j].toUpperCase());
  }
  data.languages = languages;
  var preferencesResponses = itemResponses[11].getResponse();
  if (preferencesResponses != null) {
    var preferences = "";
    for (var j = 0; j < preferencesResponses.length; j++) {
        preferences += preferencesResponses[j];
        if (j + 1 < preferencesResponses.length)
          preferences += ", ";
    }
    data.preferences = preferences;
  }
  var additionalPreferencesResponse = itemResponses[12].getResponse();
  if (additionalPreferencesResponse != null) {
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
