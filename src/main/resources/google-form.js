function onSubmit(e) {
  var data = {
      id: 0,
      createdAt: null,
      changedAt: null,
      email: null,
      phoneNumber: null,
      name: null,
      surname: null,
      residence: null,
      date: "2020-01-01",
      time: "00:00:00",
      peopleCount: 0,
      age: 0,
      gender: null,
      ageGroup: null,
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
  data.email = itemResponses[0].getResponse();
  data.phoneNumber = itemResponses[1].getResponse();
  data.name = itemResponses[2].getResponse();
  data.surname = itemResponses[3].getResponse();
  data.residence = itemResponses[4].getResponse();
  data.date = itemResponses[5].getResponse();
  data.time = itemResponses[6].getResponse() + ":00";
  data.peopleCount = itemResponses[7].getResponse();
  data.age = itemResponses[8].getResponse();
  data.gender = itemResponses[9].getResponse().toUpperCase();
  data.ageGroup = convertAgeGroup(itemResponses[10].getResponse());
  var languagesResponses = itemResponses[11].getResponse();
  var languages = [];
  for (var j = 0; j < languagesResponses.length; j++) {
    var language = {
      id: 0,
      language: "",
      meet: null
    };
    language.language = languagesResponses[j].toUpperCase();
    languages.push(language);
  }
  data.languages = languages;
  var preferencesResponses = itemResponses[12].getResponse();
  var preferences = "";
  for (var j = 0; j < preferencesResponses.length; j++) {
    preferences += preferencesResponses[j];
    if (j + 1 < preferencesResponses.length)
      preferences += ", ";
  }
  data.preferences = preferences;
  data.comment = itemResponses[13].getResponse();

  var options = {
    'method' : 'post',
    'contentType': 'application/json',
    'payload' : JSON.stringify(data)
  };
  var url = "http://localhost:8080/api/meets";
  UrlFetchApp.fetch(url, options);

  var userEmail = "meetalocaltest@gmail.com";
  var date = "2019-11-20";
  var subject = "Meet a local turisto forma";
  var message = "URL: " + url + ", POST request:" + JSON.stringify(data);

  MailApp.sendEmail (userEmail, subject, message);
}

function convertAgeGroup(ageGroup) {
  if (ageGroup == "18-29")
    return "YOUTH";
  else if (ageGroup == "30-39")
    return "JUNIOR_ADULTS";
  else if (ageGroup == "40-49")
    return "SENIOR_ADULTS";
  else
    return "SENIORS";
}