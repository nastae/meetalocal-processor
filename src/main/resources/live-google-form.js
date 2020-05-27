function onSubmit(e) {
  var data = {
      id: 0,
      createdAt: null,
      changedAt: null,
      email: null,
      phoneNumber: null,
      name: null,
      surname: null,
      country: null,
      date: "2020-01-01",
      time: "00:00:00",
      peopleCount: 0,
      age: 0,
      ageGroups: null,
      languages: [],
      preferences: "",
      comment: "",
      status: null,
      volunteer: null,
      meetEngagements: null,
      reports: null
  }
  var languages = [];
  var response = e.response;
  var itemResponses = response.getItemResponses();
  data.name = itemResponses[0].getResponse();
  data.surname = itemResponses[1].getResponse();
  data.country = itemResponses[2].getResponse();
  data.age = itemResponses[3].getResponse();
  data.email = itemResponses[4].getResponse();
  data.phoneNumber = itemResponses[5].getResponse();
  data.peopleCount = itemResponses[6].getResponse();
  data.date = itemResponses[7].getResponse();
  data.time = itemResponses[8].getResponse() + ":00";
  var ageGroupsResponses = itemResponses[9].getResponse();
  var ageGroups = [];
  for (var j = 0; j < ageGroupsResponses.length; j++) {
    ageGroups.push(toAgeGroupName(ageGroupsResponses[j]));
  }
  data.ageGroups = ageGroups;
  var languagesResponses = itemResponses[10].getResponse();
  var languages = [];
  for (var j = 0; j < languagesResponses.length; j++) {
    languages.push(languagesResponses[j].toUpperCase());
  }
  data.languages = languages;
  var preferencesResponses = itemResponses[11].getResponse();
  var preferences = "";
  for (var j = 0; j < preferencesResponses.length; j++) {
    preferences += preferencesResponses[j];
    if (j + 1 < preferencesResponses.length)
      preferences += ", ";
  }
  data.preferences = preferences;
  var comment = itemResponses[12].getResponse();
  data.comment = comment == 'Submit' ? null : itemResponses[12].getResponse();

  var options = {
    'method' : 'post',
    'contentType': 'application/json',
    'payload' : JSON.stringify(data)
  };
  var userEmail = "meetalocaltest@gmail.com";
  var date = "2019-11-20";
  var subject = "Meet a local turisto forma";
  var message = "URL: " + url + ", POST request:" + JSON.stringify(data);

  MailApp.sendEmail (userEmail, subject, message);

  var url = "https://localhost:8080/registration/meets";
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
