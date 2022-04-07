<template>
  <b-container class="regContainer" fluid>
    <b-row>
      <b-col />
      <b-col cols="6">
        <div style="text-align: center">
          <h3>Registration</h3>
        </div>
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
              data-cy="usernameInput"
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
              data-cy="accountNameInput"
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
              data-cy="pw1Input"
              placeholder="Enter password"
              required
              type="password"
            />
            <b-form-invalid-feedback :state="validationPassword" data-cy="passwordInvalidFeedback">
              Your password needs to be at least 8 chars long!
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="validationPassword" data-cy="passwordValidFeedback">
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
              data-cy="pw2Input"
              placeholder="Enter the same again"
              required
              type="password"
            />
            <b-form-invalid-feedback :state="validationPasswordEq" data-cy="passwordEqInvalidFeedback">
              Your passwords dont match!
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="validationPasswordEq" data-cy="passwordEqValidFeedback">
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
              data-cy="2faChoice"
              value="true"
              unchecked-value="false"
            >
              Yes I want to enable enchanced Security for my Account !
            </b-form-checkbox>
            <div v-if="enableSecurity === 'true'">
              <b-form-select
                v-model="firstQuestion"
                data-cy="firstQuestion"
                :options="questions"
                class="mt-2"
              ></b-form-select>
              <div v-if="firstQuestion">
                <b-form-input v-model="firstAnswer" data-cy="firstAnswerInput" class="mt-2"></b-form-input>
              </div>
              <div v-if="firstAnswer">
                <b-form-select
                  v-model="secondQuestion"
                  data-cy="secondQuestion"
                  :options="questions"
                  class="mt-2"
                ></b-form-select>
              </div>
              <div v-if="secondQuestion">
                <b-form-input
                  v-model="secondAnswer"
                  data-cy="secondAnswerInput"
                  class="mt-2"
                ></b-form-input>
              </div>
              <div v-if="secondAnswer">
                <b-form-select
                  v-model="thirdQuestion"
                  data-cy="thirdQuestion"
                  :options="questions"
                  class="mt-2"
                ></b-form-select>
              </div>
              <div v-if="thirdQuestion">
                <b-form-input v-model="thirdAnswer" data-cy="thirdAnswerInput" class="mt-2"></b-form-input>
              </div>
              <b-form-invalid-feedback :state="validationSecurityQuestions" data-cy="2faInvalidFeedback">
                You need to select 3 unique questions and answer them !
              </b-form-invalid-feedback>
              <b-form-valid-feedback :state="validationSecurityQuestions" data-cy="2faValidFeedback">
                Security questions are answered and valid !
              </b-form-valid-feedback>
            </div>
          </b-form-group>
          <b-form-group>
            <b-form-checkbox
              id="checkBoxTerms"
              v-model="statusTerms"
              data-cy="termsBtn"
              name="checkboxTerms"
              unchecked-value="not_accepted"
              value="accepted"
            >
              I accept the terms and use
            </b-form-checkbox>
            <b-form-checkbox
              id="checkBoxPrivacy"
              v-model="statusPrivacy"
              data-cy="privacyBtn"
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
            <b-button variant="success" data-cy=registerSubmitBtn @click="register()">
              Register
            </b-button>
          </div>
        </b-form>
        <b-button class="mt-2" nuxt-link to="/"> Go back </b-button>
        <div v-if="responseSuccess" class="pt-2" style="text-align: center">
          <b-alert dismissible data-cy="registerSuccess" show variant="success">
            {{ responseMassage }}
            <nuxt-link class="mt-2" nuxt-link to="/login">
              Now you can login and start tracking !
            </nuxt-link>
          </b-alert>
        </div>
        <div v-if="responseError" class="pt-2" style="text-align: center">
          <b-alert dismissible data-cy="registerError" show variant="danger">
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
  name: 'RegisterPage',
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
              securityQuestion1: this.firstQuestion,
              securityQuestion2: this.secondQuestion,
              securityQuestion3: this.thirdQuestion,
              securityAnswer1: this.firstAnswer,
              securityAnswer2: this.secondAnswer,
              securityAnswer3: this.thirdAnswer,
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
