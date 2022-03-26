// register.spec.js created with Cypress
//
// Start writing your Cypress tests below!
// If you're unfamiliar with how Cypress works,
// check out the link below and learn how to write your first test:
// https://on.cypress.io/writing-first-test


describe('register test', () => {
  it('register with valid info, no 2fa', () => {
    cy.visit('http://localhost:3000')
    cy.get('[data-cy=registerBtn]').click()

    cy.get('[data-cy=usernameInput]')
      .type('test@jttracker.de')
      .should('have.value', 'test@jttracker.de')

      cy.get('[data-cy=accountNameInput]')
      .type('Test')
      .should('have.value', 'Test')

      cy.get('[data-cy=pw1Input]')
      .type('12345678')
      .should('have.value', '12345678')

      cy.get('[data-cy=pw2Input]')
      .type('12345678')
      .should('have.value', '12345678')

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=termsBtn]').check({force: true})

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=privacyBtn]').check({force: true})

      cy.get('[data-cy=registerSubmitBtn]').click()

      cy.get('[data-cy=registerSuccess]')

  })

  it('register with valid info, if account present, no 2fa', () => {
    cy.visit('/')
    cy.get('[data-cy=registerBtn]').click()

    cy.get('[data-cy=usernameInput]')
      .type('test@jttracker.de')
      .should('have.value', 'test@jttracker.de')

      cy.get('[data-cy=accountNameInput]')
      .type('Test')
      .should('have.value', 'Test')

      cy.get('[data-cy=pw1Input]')
      .type('12345678')
      .should('have.value', '12345678')

      cy.get('[data-cy=pw2Input]')
      .type('12345678')
      .should('have.value', '12345678')

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=termsBtn]').check({force: true})

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=privacyBtn]').check({force: true})

      cy.get('[data-cy=registerSubmitBtn]').click()

      cy.get('[data-cy=registerError]')

  })

  it('register with valid info, with 2fa', () => {
    cy.visit('/')
    cy.get('[data-cy=registerBtn]').click()

    cy.get('[data-cy=usernameInput]')
      .type('test2FA@jttracker.de')
      .should('have.value', 'test2FA@jttracker.de')

      cy.get('[data-cy=accountNameInput]')
      .type('Test2FA')
      .should('have.value', 'Test2FA')

      cy.get('[data-cy=pw1Input]')
      .type('12345678')
      .should('have.value', '12345678')

      cy.get('[data-cy=pw2Input]')
      .type('12345678')
      .should('have.value', '12345678')

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=2faChoice]').check({force: true})

      cy.get('[data-cy=firstQuestion]').select('What is the name of your first pet?')

      cy.get('[data-cy=firstAnswerInput]')
      .type('test_1')
      .should('have.value', 'test_1')

      cy.get('[data-cy=secondQuestion]').select('What is the name of your first school?')


      cy.get('[data-cy=secondAnswerInput]')
      .type('test_2')
      .should('have.value', 'test_2')

      cy.get('[data-cy=thirdQuestion]').select('What is your favorite color?')


      cy.get('[data-cy=thirdAnswerInput]')
      .type('test_3')
      .should('have.value', 'test_3')

      cy.get('[data-cy=2faValidFeedback]')

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=termsBtn]').check({force: true})

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=privacyBtn]').check({force: true})

      cy.get('[data-cy=registerSubmitBtn]').click()

      cy.get('[data-cy=registerSuccess]')

  })

  it('register with validation tests', () => {
    cy.visit('/')
    cy.get('[data-cy=registerBtn]').click()

    cy.get('[data-cy=usernameInput]')
      .type('testinvalid@jttracker.de')
      .should('have.value', 'testinvalid@jttracker.de')

      cy.get('[data-cy=accountNameInput]')
      .type('TestInvalid')
      .should('have.value', 'TestInvalid')

      cy.get('[data-cy=pw1Input]')
      .type('1234567')
      .should('have.value', '1234567')

      cy.get('[data-cy=pw2Input]')
      .type('1234567')
      .should('have.value', '1234567')

      cy.get('[data-cy=passwordInvalidFeedback]')

      cy.get('[data-cy=passwordEqValidFeedback]')


      cy.get('[data-cy=pw1Input]')
      .clear()
      .type('12345678')
      .should('have.value', '12345678')

      cy.get('[data-cy=pw2Input]')
      .clear()
      .type('1234567')
      .should('have.value', '1234567')

      cy.get('[data-cy=passwordValidFeedback]')

      cy.get('[data-cy=passwordEqInvalidFeedback]')

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=termsBtn]').check({force: true})

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=privacyBtn]').check({force: true})

      // eslint-disable-next-line cypress/no-force
      cy.get('[data-cy=2faChoice]').check({force: true})

      cy.get('[data-cy=firstQuestion]').select('What is the name of your first pet?')

      cy.get('[data-cy=firstAnswerInput]')
      .type('test_1')
      .should('have.value', 'test_1')

      cy.get('[data-cy=secondQuestion]').select('What is the name of your first pet?')

      cy.get('[data-cy=secondAnswerInput]')
      .type('test_2')
      .should('have.value', 'test_2')

      cy.get('[data-cy=thirdQuestion]').select('What is your favorite color?')


      cy.get('[data-cy=thirdAnswerInput]')
      .type('test_3')
      .should('have.value', 'test_3')

      cy.get('[data-cy=2faInvalidFeedback]')

      cy.get('[data-cy=secondQuestion]').select('What is your favorite date in history?')

      cy.get('[data-cy=secondAnswerInput]')
      .clear()
      .type('test_2')
      .should('have.value', 'test_2')

      cy.get('[data-cy=registerSubmitBtn]').click()

      cy.get('[data-cy=registerSuccess]')


  })



})
