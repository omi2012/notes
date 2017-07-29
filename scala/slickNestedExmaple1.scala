case class Patient(
                    id: String = java.util.UUID.randomUUID().toString,
                    companyScopeId: String,
                    assignedToUserId: Option[String] = None,
                    info: PatientInfo
                    ) extends ModelWithId



case class PatientInfo(
                        firstName: Option[String] = None,
                        lastName: Option[String] = None,
                        gender: Option[Gender.Value] = None,
                        alias: Option[String] = None,
                        street: Option[String] = None,
                        city: Option[String] = None,
                        postalCode: Option[String] = None,
                        phone: Option[String] = None,
                        mobilePhone: Option[String] = None,
                        email: Option[String] = None,
                        age: Option[AgeRange.Value] = None,
                        companySeniority: Option[CompanySeniorityRange.Value] = None,
                        employmentContract: Option[EmploymentContract.Value] = None,
                        socialStatus: Option[SocialStatus.Value] = None,
                        jobTitle: Option[String] = None
                        )

class PatientTable(tag: Tag) extends TableWithId[Patient](tag,"PATIENT") {
  override def id = column[String]("id", O.PrimaryKey)
  def companyScopeId = column[String]("company_scope_id", O.NotNull)
  def assignedToUserId = column[Option[String]]("assigned_to_user_id", O.Nullable)

  def firstName = column[Option[String]]("first_name", O.Nullable)
  def lastName = column[Option[String]]("last_name", O.Nullable)
  def gender = column[Option[Gender.Value]]("gender", O.Nullable)
  def alias = column[Option[String]]("alias", O.Nullable)
  def street = column[Option[String]]("street", O.Nullable)
  def city = column[Option[String]]("city", O.Nullable)
  def postalCode = column[Option[String]]("postal_code", O.Nullable)
  def phone = column[Option[String]]("phone", O.Nullable)
  def mobilePhone = column[Option[String]]("mobile_phone", O.Nullable)
  def email = column[Option[String]]("email", O.Nullable)
  def age = column[Option[AgeRange.Value]]("age", O.Nullable)
  def companySeniority = column[Option[CompanySeniorityRange.Value]]("company_seniority", O.Nullable)
  def employmentContract = column[Option[EmploymentContract.Value]]("employment_contract", O.Nullable)
  def socialStatus = column[Option[SocialStatus.Value]]("social_status", O.Nullable)
  def jobTitle = column[Option[String]]("job_title", O.Nullable)
  def role = column[Option[String]]("role", O.Nullable)



  private type PatientInfoTupleType = (Option[String], Option[String], Option[Gender.Value], Option[String], Option[String], Option[String], Option[String], Option[String], Option[String], Option[String], Option[AgeRange.Value], Option[CompanySeniorityRange.Value], Option[EmploymentContract.Value], Option[SocialStatus.Value], Option[String])
  private type PatientTupleType = (String, String, Option[String], PatientInfoTupleType)
  //
  private val patientShapedValue = (id, companyScopeId, assignedToUserId,
    (
      firstName, lastName, gender, alias, street, city, postalCode,
      phone, mobilePhone,email, age, companySeniority, employmentContract, socialStatus, jobTitle
      )
    ).shaped[PatientTupleType]
  //
  private val toModel: PatientTupleType => Patient = { patientTuple =>
    Patient(
      id = patientTuple._1,
      companyScopeId = patientTuple._2,
      assignedToUserId = patientTuple._3,
      info = PatientInfo.tupled.apply(patientTuple._4)
    )
  }
  private val toTuple: Patient => Option[PatientTupleType] = { patient =>
    Some {
      (
        patient.id,
        patient.companyScopeId,
        patient.assignedToUserId,
        (PatientInfo.unapply(patient.info).get)
        )
    }
  }

  def * = patientShapedValue <> (toModel,toTuple)
}