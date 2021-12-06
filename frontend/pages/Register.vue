<template>
  <b-container class="regContainer" fluid>
    <b-row>
      <b-col />
      <b-col cols="6">
        <h4>Registration</h4>
        <b-form>
          <b-form-group
            id="input-group-email"
            description="!Following will become your Username!"
            label="Email address:"
            label-for="input-email"
          >
            <b-form-input
              id="input-email"
              v-model="email"
              placeholder="Enter email"
              required
              type="email"
            />
          </b-form-group>
          <b-form-group
            id="input-group-accountName"
            label="Your account name:"
            label-for="input-accountName"
          >
            <b-form-input
              id="input-accountName"
              v-model="accountName"
              placeholder="Enter the name of your Account"
              required
            />
          </b-form-group>
          <b-form-group
            id="input-group-pw1"
            label="Enter password:"
            label-for="input-pw1"
          >
            <b-form-input
              id="input-pw1"
              v-model="pw1"
              placeholder="Enter password"
              required
              type="password"
            />
            <b-form-invalid-feedback :state="validationPassword">
              Your password needs to be at least 8 chars long!
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="validationPassword">
              Looks Good.
            </b-form-valid-feedback>
          </b-form-group>
          <b-form-group
            id="input-group-pw2"
            label="Enter again:"
            label-for="input-pw2"
          >
            <b-form-input
              id="input-pw2"
              v-model="pw2"
              placeholder="Enter the same again"
              required
              type="password"
            />
            <b-form-invalid-feedback :state="validationPasswordEq">
              Your passwords dont match!
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="validationPasswordEq">
              Passwords match!
            </b-form-valid-feedback>
          </b-form-group>
          <b-form-group
            id="input-group-2fa"
            label="Security-Question-Selection:"
            label-for="choice-2fa"
          >
            <b-form-checkbox
              id="choice-2fa"
              v-model="enableSecurity"
              name="checkbox-1"
              value="true"
              unchecked-value="false"
            >
              Yes I want to enable enchanced Security for my Account !
            </b-form-checkbox>
            <div v-if="enableSecurity === 'true'">
              <b-form-select
                v-model="firstQuestion"
                :options="questions"
                class="mt-2"
              ></b-form-select>
              <div v-if="firstQuestion">
                <b-form-input v-model="firstAnswer" class="mt-2"></b-form-input>
              </div>
              <div v-if="firstAnswer">
                <b-form-select
                  v-model="secondQuestion"
                  :options="questions"
                  class="mt-2"
                ></b-form-select>
              </div>
              <div v-if="secondQuestion">
                <b-form-input
                  v-model="secondAnswer"
                  class="mt-2"
                ></b-form-input>
              </div>
              <div v-if="secondAnswer">
                <b-form-select
                  v-model="thirdQuestion"
                  :options="questions"
                  class="mt-2"
                ></b-form-select>
              </div>
              <div v-if="thirdQuestion">
                <b-form-input v-model="thirdAnswer" class="mt-2"></b-form-input>
              </div>
              <b-form-invalid-feedback :state="validationSecurityQuestions">
                You need to select 3 unique questions and answer them !
              </b-form-invalid-feedback>
              <b-form-valid-feedback :state="validationSecurityQuestions">
                Security questions are answered and valid !
              </b-form-valid-feedback>
            </div>
          </b-form-group>
          <b-form-group>
            <b-form-checkbox
              id="checkBoxTerms"
              v-model="statusTerms"
              name="checkboxTerms"
              unchecked-value="not_accepted"
              value="accepted"
            >
              I accept the terms and use
            </b-form-checkbox>
            <b-form-checkbox
              id="checkBoxPrivacy"
              v-model="statusPrivacy"
              name="checkboxPrivacy"
              unchecked-value="not_accepted"
              value="accepted"
            >
              I read and accept privacy-terms (DSGVO)
            </b-form-checkbox>
          </b-form-group>
          <div
            v-if="
              (email &&
                accountName &&
                pw1 &&
                pw2 &&
                statusTerms === 'accepted' &&
                statusPrivacy === 'accepted' &&
                enableSecurity === 'false') ||
              (email &&
                accountName &&
                pw1 &&
                pw2 &&
                statusTerms === 'accepted' &&
                statusPrivacy === 'accepted' &&
                enableSecurity === 'true' &&
                firstAnswer &&
                secondAnswer &&
                thirdAnswer &&
                validationSecurityQuestions)
            "
          >
            <b-button variant="success" @click="register()">
              Register
            </b-button>
          </div>
        </b-form>
        <b-button class="mt-2" nuxt-link to="/"> Go back </b-button>
        <div v-if="responseSuccess" class="pt-2" style="text-align: center">
          <b-alert dismissible show variant="success">
            {{ responseMassage }}
            <b-button class="mt-2" nuxt-link to="/login">
              Now you can login and start tracking !
            </b-button>
          </b-alert>
        </div>
        <div v-if="responseError" class="pt-2" style="text-align: center">
          <b-alert dismissible show variant="danger">
            {{ responseError }}
          </b-alert>
        </div>
      </b-col>
      <b-col />
    </b-row>
  </b-container>
</template>

<script>
export default {
  asyncData() {
    return {
      email: '',
      accountName: '',
      pw1: '',
      pw2: '',
      statusTerms: 'not_accepted',
      statusPrivacy: 'not_accepted',
      responseSuccess: '',
      responseError: '',
      enableSecurity: 'false',
      firstQuestion: null,
      secondQuestion: null,
      thirdQuestion: null,
      firstAnswer: null,
      secondAnswer: null,
      thirdAnswer: null,
      questions: [
        { value: null, text: 'Select a question !' },
        {
          value: 'What is the name of your first pet?',
          text: 'What is the name of your first pet?',
        },
        {
          value: 'What is the name of your first school?',
          text: 'What is the name of your first school?',
        },
        { value: 'What was your first job?', text: 'What was your first job?' },
        {
          value: 'What is your favorite color?',
          text: 'What is your favorite color?',
        },
        {
          value: 'What is your favorite date in history?',
          text: 'What is your favorite date in history?',
        },
      ],
    }
  },
  computed: {
    validationPassword() {
      return this.pw1.length >= 8
    },
    validationPasswordEq() {
      return this.pw1 === this.pw2
    },
    validationSecurityQuestions() {
      return (
        this.firstQuestion !== this.secondQuestion &&
        this.firstQuestion !== this.thirdQuestion &&
        this.secondQuestion !== this.thirdQuestion &&
        this.firstAnswer &&
        this.secondAnswer &&
        this.thirdAnswer
      )
    },
  },
  methods: {
    async register() {
      try {
        if (this.enableSecurity === 'false') {
          const responseWithoutSecurityQuestions = await this.$axios.post(
            '/register',
            {
              username: this.email,
              accountName: this.accountName,
              password: this.pw2,
            }
          )
          if (responseWithoutSecurityQuestions.data.validated === true) {
            this.responseSuccess =
              responseWithoutSecurityQuestions.data.successMessage
          } else {
            this.responseError =
              responseWithoutSecurityQuestions.data.errorMessage
          }
        } else {
          const responseWithSecurityQuestions = await this.$axios.post(
            '/register',
            {
              username: this.email,
              accountName: this.accountName,
              password: this.pw2,
              securityQuestionsAvailable: this.enableSecurity,
              securityQuestion_1: this.firstQuestion,
              securityQuestion_2: this.secondQuestion,
              securityQuestion_3: this.thirdQuestion,
              securityAnswer_1: this.firstAnswer,
              securityAnswer_2: this.secondAnswer,
              securityAnswer_3: this.thirdAnswer,
            }
          )
          if (responseWithSecurityQuestions.data.validated === true) {
            this.responseSuccess =
              responseWithSecurityQuestions.data.successMessage
          } else {
            this.responseError = responseWithSecurityQuestions.data.errorMessage
          }
        }
      } catch (e) {
        alert(e.toString())
        window.location.reload()
      }
    },
  },
}
</script>
