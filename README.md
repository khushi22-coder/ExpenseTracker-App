# Task 1 – Project Planning, Requirement Analysis & Environment Setup

## Overview

This phase focused on planning and preparing the foundation of the Android application. The objective was to establish a production-ready development environment, define the project scope, identify core features, create user personas, and set up project documentation and version control.

## Project Selected

**Expense Tracker App**

The Expense Tracker App helps users manage their daily expenses, track spending habits, categorize transactions, and monitor financial goals through an intuitive and user-friendly interface.

## Minimum Viable Product (MVP) Features

1. User Registration and Login
2. Add Income and Expense Records
3. Categorize Transactions
4. View Transaction History
5. Dashboard with Expense Summary
6. Monthly Spending Analysis
7. Search and Filter Transactions
8. User Profile Management
9. Settings and Preferences
10. Local Data Storage

## User Personas

### Persona 1: College Student

* Name: Rahul
* Age: 20
* Goal: Track daily expenses and save money.
* Journey:

  * Sign up
  * Add daily expenses
  * View monthly spending summary
  * Monitor savings progress

### Persona 2: Working Professional

* Name: Priya
* Age: 28
* Goal: Manage personal finances efficiently.
* Journey:

  * Log in
  * Record income and expenses
  * Categorize transactions
  * Analyze spending reports

## Environment Setup

* Installed Android Studio (Latest Stable Version)
* Configured Android SDK (API 33 & API 34)
* Created Pixel 6 Emulator (API 33)
* Set up Git and GitHub Repository
* Created Main and Development Branches
* Configured Project Structure

## Project Architecture

com.apexplanet.expensetracker/
├── data/
├── domain/
├── presentation/
└── utils/

### Package Responsibilities

#### data/

Handles local database, repositories, and remote data sources.

#### domain/

Contains business logic, models, and use cases.

#### presentation/

Contains Activities, Fragments, ViewModels, Adapters, and UI-related components.

#### utils/

Contains helper classes, extensions, constants, and utility functions.

## Documentation & Planning

* Prepared Project Requirements Document (PRD)
* Created Trello/Notion Board
* Defined User Stories and Development Roadmap
* Configured .gitignore
* Created README.md
* Established Coding Standards and Conventions

## Deliverables

* Android Studio Environment Configured
* GitHub Repository Initialized
* Project Architecture Defined
* PRD Completed
* User Personas Created
* MVP Features Finalized
* Development Workflow Established

## Learning Outcomes

* Android Project Planning
* Requirement Analysis
* Git & GitHub Workflow
* Clean Architecture Setup
* Agile Project Documentation


# Task 2 – Designing & Implementing UI/UX

## Overview

This phase focused on designing a modern and user-friendly interface using Material Design 3 principles and implementing responsive Android layouts. The goal was to create visually appealing screens, ensure smooth navigation, and provide an excellent user experience.

## UI/UX Design Process

### Research & Planning

* Studied Material Design 3 Guidelines
* Analyzed modern Android application design patterns
* Planned user flow and navigation structure

### Wireframing

Created low-fidelity wireframes for:

* Splash Screen
* Onboarding Screens
* Login Screen
* Signup Screen
* Dashboard Screen
* Transaction Details Screen
* Profile Screen
* Settings Screen

### High-Fidelity Design

Designed complete UI screens in Figma with:

* Consistent color palette
* Typography hierarchy
* Responsive layouts
* Material Design components

## Design System

### Color Palette

* Primary Color
* Secondary Color
* Error Color
* Background Colors
* Surface Colors

### Typography

* Headings
* Subheadings
* Body Text
* Labels

### Spacing System

* 8dp Grid System
* Consistent Margins and Padding
* Responsive Layout Structure

## Android UI Implementation

### Layout Development

* Implemented XML layouts using ConstraintLayout
* Enabled ViewBinding
* Created reusable UI components
* Added responsive screen support

### Screens Implemented

1. Splash Screen
2. Onboarding Screen 1
3. Onboarding Screen 2
4. Onboarding Screen 3
5. Login Screen
6. Signup Screen
7. Dashboard Screen
8. Transaction Details Screen
9. Profile Screen
10. Settings Screen

## Features Implemented

### Splash Screen

* Android 12+ SplashScreen API
* Smooth startup experience

### Onboarding

* ViewPager2 Integration
* Page Indicators
* Introductory User Guidance

### Authentication UI

* Login Form
* Signup Form
* Input Validation
* Error Handling

### Dashboard

* RecyclerView Integration
* CardView Design
* Floating Action Button (FAB)
* Transaction Listing

### Navigation

* Navigation Component Setup
* Bottom Navigation / Drawer Navigation
* Smooth Screen Transitions

### Material Design Components

* Material Buttons
* TextInputLayout
* TextInputEditText
* Chips
* Switches
* Snackbars
* Progress Indicators
* Custom Dialogs

## User Experience Enhancements

* Form Validation
* Error Messages
* Loading Indicators
* Feedback Snackbars
* Intuitive Navigation Flow
* Responsive Design

## Deliverables

* Figma Design Files
* Interactive Prototype
* XML Layouts
* Material Design Themes
* Drawable Assets
* Screen Screenshots
* Navigation Setup
* ViewBinding Configuration

## Learning Outcomes

* Figma UI/UX Design
* Material Design 3
* ConstraintLayout
* ViewPager2
* RecyclerView
* Navigation Component
* Responsive Android Design
* User-Centered Design Principles
