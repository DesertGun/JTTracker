// auth.spec.js created with Cypress
//
// Start writing your Cypress tests below!
// If you're unfamiliar with how Cypress works,
// check out the link below and learn how to write your first test:
// https://on.cypress.io/writing-first-test


describe('auth test', () => {
  it('login with correct info', () => {
    cy.visit('http://localhost:3000')
    cy.get('#loginBtn').click()
    cy.get('#usernameInput')
      .type('main@jttracker.de')
      .should('have.value', 'main@jttracker.de')

      cy.get('#passwordInput')
      .type('password')
      .should('have.value', 'password')

      cy.get('#loginBtn').click()

      cy.url().should('eq', 'http://localhost:3000/')
  })

  it('login with incorrect info', () => {
    cy.visit('http://localhost:3000')
    cy.get('#loginBtn').click()
    cy.get('#usernameInput')
      .type('main@jttracker.de')
      .should('have.value', 'main@jttracker.de')

      cy.get('#passwordInput')
      .type('12345678')
      .should('have.value', '12345678')

      cy.get('#loginBtn').click()

      cy.get('#loginError')

      cy.url().should('eq', 'http://localhost:3000/login')
  })

})
